package online.be.service;

import online.be.config.BusinessRuleConfig;
import online.be.entity.*;
import online.be.enums.*;
import online.be.exception.BadRequestException;
import online.be.exception.NoDataFoundException;
import online.be.model.CheckedSlotDetail;
import online.be.model.FlexibleTimeSlot;
import online.be.model.Request.DailyScheduleBookingRequest;
import online.be.model.Request.FixedScheduleBookingRequest;
import online.be.model.Request.FlexibleBookingRequest;

import online.be.model.Response.BookingDetailResponse;
import online.be.model.Response.BookingResponse;
import online.be.model.Response.CheckInResponse;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepo;

    @Autowired
    BookingDetailRepositiory bookingDetailRepo;

    @Autowired
    TimeSlotRepository timeSlotRepo;

    @Autowired
    BookingDetailService detailService;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    CourtTimeSlotRepository courtTimeSlotRepo;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CourtRepository courtRepo;

    @Autowired
    VenueRepository venueRepo;

    @Autowired
    EmailService emailService;

    @Autowired
    BookingDetailService bookingDetailService;

    @Autowired
    BusinessRuleConfig businessRuleConfig;

    public BookingResponse createDailyScheduleBooking(DailyScheduleBookingRequest bookingRequest) {
        Account currentAccount = authenticationService.getCurrentAccount();
        LocalDate bookingDate = LocalDate.now();
        LocalDate checkInDate = LocalDate.parse(bookingRequest.getCheckInDate());

        // Retrieve and sort the timeslots based on startTime
        List<Long> slotIds = bookingRequest.getTimeslot();
        List<TimeSlot> timeSlots = new ArrayList<>();

        for (Long slotId : slotIds) {
            TimeSlot timeSlot = timeSlotRepo.findById(slotId)
                    .orElseThrow(() -> new BadRequestException("Timeslot not found: " + slotId));
            timeSlots.add(timeSlot);
        }

//        timeSlots.sort(new Comparator<TimeSlot>() {
//            @Override
//            public int compare(TimeSlot ts1, TimeSlot ts2) {
//                return ts1.getStartTime().compareTo(ts2.getStartTime());
//            }
//        });


        // Retrieve court by id
        Court court = courtRepo.findById(bookingRequest.getCourt())
                .orElseThrow(() -> new BadRequestException("Court not found with id: " + bookingRequest.getCourt()));
        Venue venue = court.getVenue();
        long venueId = venue.getId();
        // Create booking
        Booking booking = new Booking();
        List<BookingDetail> details = new ArrayList<>();
        double totalPrice = 0;
        int totalHours = 0;

        // Create a booking detail for each timeslot
        for (TimeSlot timeSlot : timeSlots) {
            BookingDetail detail = detailService.createBookingDetail(BookingType.DAILY, checkInDate, court, timeSlot);
            detail.setBooking(booking);
            details.add(detail);
            totalPrice += detail.getPrice();
            totalHours += (int) detail.getDuration();
        }
        // Set booking details and save
        try {
            booking.setAccount(currentAccount);
            booking.setBookingDate(bookingDate);
            booking.setBookingType(BookingType.DAILY);
            booking.setTotalPrice(totalPrice);
            booking.setTotalTimes(totalHours);
            booking.setBookingDetailList(details);
            booking.setApplicationDate(checkInDate);
            booking.setStatus(BookingStatus.BOOKED);
            booking.setVenueId(venueId);
            bookingRepo.save(booking);
            processBookingPayment(booking.getId(), venueId);
            BookingResponse response = mapToBookingResponse(booking);
            return response;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("This slot is booked.");
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format for check-in date: " + e.getParsedString());
        } catch (NoDataFoundException e) {
            throw new BadRequestException("No data found: " + e.getMessage());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public BookingResponse createFixedScheduleBooking(FixedScheduleBookingRequest bookingRequest) {
        Account currentAccount = authenticationService.getCurrentAccount();
        LocalDate applicationStartDate = LocalDate.parse(bookingRequest.getApplicationStartDate());
        LocalDate bookingDate = LocalDate.now();
        Court court = courtRepo.findById(bookingRequest.getCourt()).orElseThrow(() ->
                new BadRequestException("Court not found with id: " + bookingRequest.getCourt()));

        Venue venue = court.getVenue();

        Booking booking = new Booking();
        List<BookingDetail> details = new ArrayList<>();

        for (String dayOfWeekStr : bookingRequest.getDayOfWeek()) {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayOfWeekStr.toUpperCase());

            List<TimeSlot> timeSlots = new ArrayList<>();
            for (Long timeslotId : bookingRequest.getTimeslot()) {
                TimeSlot timeSlot = timeSlotRepo.findById(timeslotId).orElseThrow(() ->
                        new BadRequestException("Timeslot not found: " + timeslotId));
                timeSlots.add(timeSlot);
            }
            for (int month = 0; month < bookingRequest.getDurationInMonths(); month++) {
                LocalDate endOfMonth = applicationStartDate.plusDays(29);

                LocalDate firstBookingDay = applicationStartDate.with(TemporalAdjusters.nextOrSame(dayOfWeek));

                for (LocalDate date = firstBookingDay; !date.isAfter(endOfMonth); date = date.plusWeeks(1)) {
                    for (TimeSlot timeSlot : timeSlots) {
                        BookingDetail detail = detailService.createBookingDetail(BookingType.FIXED, date, court, timeSlot);
                        detail.setBooking(booking);
                        details.add(detail);
                    }
                }
            }
        }

        double totalPrice = 0.0;
        int totalHours = 0;
        for (BookingDetail detail : details) {
            totalPrice += detail.getPrice();
            totalHours += (int) (detail.getDuration() / 60);
        }

        try {
            booking.setAccount(currentAccount);
            booking.setBookingDate(bookingDate);
            booking.setTotalPrice(totalPrice);
            booking.setTotalTimes(totalHours);
            booking.setBookingDetailList(details);
            booking.setStatus(BookingStatus.BOOKED);
            booking.setBookingType(BookingType.FIXED);
            booking.setApplicationDate(applicationStartDate);
            booking.setVenueId(venue.getId());
            bookingRepo.save(booking);
            return mapToBookingResponse(booking);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("This slot is booked.");
        }
    }

    public CheckInResponse checkIn(long id, String date) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        Account account = booking.getAccount();
        List<BookingDetail> details = booking.getBookingDetailList();
        LocalDate checkInDate = LocalDate.parse(date);

        List<CheckedSlotDetail> checkedSlots = new ArrayList<>();

        for (BookingDetail detail : details) {
            if (detail.getCourtTimeSlot().getCheckInDate().equals(checkInDate)) {
                CourtTimeSlot courtTimeSlot = courtTimeSlotRepo.findByBookingDetail(detail);
                courtTimeSlot.setStatus(SlotStatus.CHECKED);
                courtTimeSlotRepo.save(courtTimeSlot);

                // Add checked slot details to the response
                CheckInResponse checkInResponse = new CheckInResponse();
                CheckedSlotDetail slotDetail = new CheckedSlotDetail();
                slotDetail.setCourtTimeSlotId(courtTimeSlot.getId());
                slotDetail.setCheckInDate(courtTimeSlot.getCheckInDate());
                slotDetail.setSlotStatus(courtTimeSlot.getStatus());
                checkedSlots.add(slotDetail);
            }
        }

        // Check if all details are checked
        if (checkedSlots.size() == 0) {
            booking.setStatus(BookingStatus.CONFIRMED);
        }
        bookingRepo.save(booking);

        CheckInResponse response = new CheckInResponse();
        response.setBookingId(booking.getId());
        response.setBookingStatus(booking.getStatus());
        response.setCheckedSlotDetails(checkedSlots);
        response.setPhoneNumber(account.getPhone());
        response.setEmail(account.getEmail());
        return response;
    }

    public void deleteBooking(Long bookingId) {
        bookingRepo.deleteById(bookingId);
    }
    //Tự tạo hiển thị không có Booking

    //mua số giờ chơi cho loại lịch linh hoạt
    public Booking purchaseFlexibleHours(int totalHour, long venueId, String date) {
        // Load current user
        Account user = authenticationService.getCurrentAccount();
        LocalDate applicationDate = LocalDate.parse(date);
        // Validate total hour
        if (totalHour <= 0) {
            throw new BadRequestException("Total hour must be positive");
        }

        Venue venue = venueRepo.findById(venueId).orElseThrow(() ->
                new BadRequestException("Venue not found")
        );

        // Retrieve pricing for flexible booking
//        double flexiblePricePerHour = venue.getPricingList().stream()
//                .filter(p -> p.getBookingType().equals(BookingType.FLEXIBLE))
//                .mapToDouble(Pricing::getPricePerHour)
//                .findFirst()
//                .orElseThrow(() -> new BadRequestException("Flexible pricing not found"));

        double flexiblePricePerHour = -1;
        for (Pricing pricing : venue.getPricingList()){
            if(pricing.getBookingType().equals(BookingType.FLEXIBLE)){
                flexiblePricePerHour = pricing.getPricePerHour();
                break;
            }
        }
        if(flexiblePricePerHour == -1){
            throw new BadRequestException("Flexible pricing not found");
        }
        //calculate price
        double totalPrice = flexiblePricePerHour * totalHour;
        // Create booking entity
        Booking booking = new Booking();
        booking.setAccount(user);
        booking.setBookingType(BookingType.FLEXIBLE);
        booking.setBookingDate(LocalDate.now());
        booking.setStatus(BookingStatus.BOOKED);
        booking.setTotalTimes(totalHour);
        booking.setRemainingTimes(totalHour);
        booking.setApplicationDate(applicationDate);
        booking.setTotalPrice(totalPrice);
        booking.setVenueId(venueId);

        // Save booking
        try {
            Booking savedBooking = bookingRepo.save(booking);
            processBookingPayment(savedBooking.getId(), venueId);
            return savedBooking;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("This slot is booked.");
        }
    }

    public BookingResponse createFlexibleScheduleBooking(FlexibleBookingRequest request) {
        Account user = authenticationService.getCurrentAccount();
        Booking booking = bookingRepo.findBookingById(request.getBookingId());
        List<BookingDetail> details = new ArrayList<>();
        LocalDate applicationDate = booking.getApplicationDate();
        int duration = 0;
        try {
            for (FlexibleTimeSlot flexibleTimeSlot : request.getFlexibleTimeSlots()) {
                LocalDate endOfMonth = booking.getApplicationDate().plusDays(29);
                List<TimeSlot> timeSlots = new ArrayList<>();
                for (Long timeslotId : flexibleTimeSlot.getTimeslot()) {
                    TimeSlot timeSlot = timeSlotRepo.findById(timeslotId).orElseThrow(() ->
                            new BadRequestException("Timeslot not found: " + timeslotId));
                    timeSlots.add(timeSlot);
                }

                Court court = courtRepo.findById(flexibleTimeSlot.getCourt()).orElseThrow(() -> new BadRequestException("Court not found with id: " + flexibleTimeSlot.getCourt()));
                LocalDate checkInDate = LocalDate.parse(flexibleTimeSlot.getCheckInDate());
                if (checkInDate.isAfter(endOfMonth)) {
                    throw new BadRequestException("Check-in Date is over the month you bought");
                }

                for (TimeSlot slot : timeSlots) {
                    BookingDetail detail = detailService.createBookingDetail(BookingType.FLEXIBLE, checkInDate, court, slot);
                    detail.setBooking(booking);
                    duration = (int) detail.getDuration();

                    if(booking.getRemainingTimes()  < duration){
                        throw new BadRequestException("You do not have enough remaining times");
                    }
                    details.add(detail);
                    booking.setRemainingTimes(booking.getRemainingTimes() - duration);
                }
            }

            booking.setBookingDetailList(details);
            bookingRepo.save(booking);
            BookingResponse response = mapToBookingResponse(booking);
            sendFlexibleBookingConfirmationEmail(user, booking);
            return  response;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Failed to save booking: " + e.getMessage());
        }
    }

    private void sendFlexibleBookingConfirmationEmail(Account customer, Booking booking) {
        // Check for null parameters to avoid NullPointerException
        if (customer == null || booking == null) {
            throw new IllegalArgumentException("Customer and booking must not be null");
        }

        Venue venue = venueRepo.findById(booking.getVenueId()).get();
        if(venue == null){
            throw new BadRequestException("Venue not found");
        }
        int totalPlayHours = booking.getTotalTimes();
        List<BookingDetail> bookingDetails = booking.getBookingDetailList();

        // Build start times string if bookingDetails is not null
        StringBuilder startTimesString = new StringBuilder();
        if (bookingDetails != null) {
            for (BookingDetail detail : bookingDetails) {
                startTimesString.append(String.format("Ngày: %s, Giờ bắt đầu: %s, Sân: %s\n",
                        detail.getCourtTimeSlot().getCheckInDate(),
                        detail.getCourtTimeSlot().getTimeSlot().getStartTime(),
                        detail.getCourtTimeSlot().getCourt().getCourtName()));
            }
        }

        // Create subject and description of the email
        String subject = "Xác nhận đặt chỗ thành công cho lịch linh hoạt";
        StringBuilder description = new StringBuilder(String.format(
                "Kính gửi %s,\n\nĐặt chỗ của bạn đã được xác nhận thành công.\n\n" +
                        "Địa điểm: %s\n\n" +
                        "Tổng số giờ chơi: %d giờ\n\n " +
                        "Thời gian còn lại: %d giờ",
                customer.getFullName(), venue.getName(), totalPlayHours, booking.getRemainingTimes()));

        // Add start times information if available
        if (startTimesString.length() > 0) {
            description.append("Thời gian và sân:\n").append(startTimesString).append("\n");
        }

        description.append("Cảm ơn bạn đã đặt chỗ!\n\nTrân trọng,\nGoodminton Online");

        // Send email
        emailService.sendMail(customer, subject, description.toString());
    }

    private Transaction processBookingPayment(long bookingId, long venueId) {
        // Start transaction
        try {
            // Search for the booking
            Booking booking = bookingRepo.findBookingById(bookingId);
            if (booking == null) {
                throw new BadRequestException("Booking not found");
            }

            // Search for the user's wallet
            Account customer = authenticationService.getCurrentAccount();
            Wallet customerWallet = customer.getWallet();

            // Get the admin wallet
            List<Account> adminList = accountRepository.findByRole(Role.ADMIN);
            if (adminList.isEmpty()) {
                throw new BadRequestException("Admin account not found");
            }
            Account admin = adminList.get(0);
            Wallet adminWallet = admin.getWallet();

            // Ensure customer and admin do not use the same wallet
            if (customerWallet.equals(adminWallet)) {
                throw new BadRequestException("Cannot process payment using the same wallet for customer and admin.");
            }

            double amount = booking.getTotalPrice();
            if (customerWallet.getBalance() < amount) {
                throw new BadRequestException("Customer does not have enough balance.");
            }

            // Process payment
            updateBalances(customerWallet, adminWallet, amount);
            booking.setStatus(BookingStatus.BOOKED);
            bookingRepo.save(booking);

            // Create and save the transaction
            Venue venue = venueRepo.findVenueById(venueId);
            if (venue == null) {
                throw new BadRequestException("Venue not found");
            }

            Transaction transaction = createTransaction(customerWallet, adminWallet, booking, amount, venue);
            transactionRepository.save(transaction);

            // Send email notification
            sendPaymentConfirmationEmail(customer, amount, bookingId, venue);

            return transaction;
        } catch (Exception e) {
            // Rollback transaction if necessary and handle exception
            throw new RuntimeException("Error processing booking payment", e);
        }
    }

    private void updateBalances(Wallet customerWallet, Wallet adminWallet, double amount) {
        if (customerWallet.getBalance() < amount) {
            throw new BadRequestException("Failed to create booking: Insufficient funds");
        }
        customerWallet.setBalance(customerWallet.getBalance() - (float) amount);
        adminWallet.setBalance(adminWallet.getBalance() + (float) amount);
        walletRepository.save(adminWallet);
        walletRepository.save(customerWallet);
    }

    private Transaction createTransaction(Wallet fromWallet, Wallet toWallet, Booking booking, double amount, Venue venue) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.now().toString());
        transaction.setFrom(fromWallet);
        transaction.setTo(toWallet);
        transaction.setBooking(booking);
        transaction.setVenueId(venue.getId());
        transaction.setTransactionType(TransactionEnum.COMPLETED);
        transaction.setAmount((float) amount);
        transaction.setDescription("Pay for booking");
        return transaction;
    }


    private void sendPaymentConfirmationEmail(Account customer, double amount, long bookingId, Venue venue) {
        // Kiểm tra các tham số null để tránh NullPointerException
        if (customer == null || venue == null) {
            throw new IllegalArgumentException("Khách hàng và địa điểm không được để trống");
        }

        // Lấy booking để có tổng số giờ chơi
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("ID booking không hợp lệ: " + bookingId));

        // Lấy tổng số giờ chơi
        int totalPlayHours = booking.getTotalTimes();

        // Lấy chi tiết booking để có thời gian bắt đầu
        List<BookingDetail> bookingDetails = bookingDetailRepo.findByBookingId(bookingId);

        // Xây dựng chuỗi thời gian bắt đầu nếu bookingDetails không null
        StringBuilder startTimesString = new StringBuilder();
        if (bookingDetails != null) {
            for (BookingDetail detail : bookingDetails) {
                startTimesString.append(String.format("Ngày: %s, Giờ bắt đầu: %s\n",
                        detail.getCourtTimeSlot().getCheckInDate(),
                        detail.getCourtTimeSlot().getTimeSlot().getStartTime()));
            }
        }

        // Định dạng số tiền đến 2 chữ số thập phân
        String formattedAmount = String.format("%.2f", amount);

        // Tạo tiêu đề và nội dung email
        String subject = "Xác nhận booking";
        StringBuilder description = new StringBuilder(String.format(
                "Kính gửi %s,\n\nThanh toán của bạn với số tiền %s cho booking mới đã được xử lý thành công.\n\n" +
                        "Địa điểm: %s\n\n" +
                        "Tổng số giờ chơi: %d giờ\n\n",
                customer.getFullName(), formattedAmount, bookingId, venue.getName(), totalPlayHours));

        // Thêm thông tin thời gian bắt đầu nếu có
        if (startTimesString.length() > 0) {
            description.append("Thời gian bắt đầu:\n").append(startTimesString).append("\n");
        }

        description.append("Cảm ơn bạn đã đặt chỗ!\n\nTrân trọng,\nGoodminton Online");

        // Gửi email
        emailService.sendMail(customer, subject, description.toString());
    }

    //cancel booking
    public Object requestCancelBooking(long bookingId, long bookingDetailId){
        //check booking is exist or not
        Booking booking = bookingRepo.findBookingById(bookingId);
        if(booking == null){
            throw new BadRequestException("Booking not found");
        }

        // Check if booking is already cancelled
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled and cannot be cancelled again");
        }
        //check the validate of booking
        if(!isValidCancellation(booking)){
            //nếu không đủ điều kiện để hủy thì sẽ hoàn tiền
            throw new BadRequestException("Cannot cancel the booking." +
                    "The cancellation window has passed");
        }

        if (booking.getBookingType().equals(BookingType.FLEXIBLE)){
            BookingDetail detail = bookingDetailRepo.findById(bookingDetailId).get();
            return confirmRefundFlexible(booking,detail);
        }else {
            return confirmRefund(booking);
        }

    }

    public Transaction confirmRefund(Booking booking) {
        //search user wallet
        Account customer = authenticationService.getCurrentAccount();//lấy  tài khoản hiện tại
        //kiểm tra xem customer này có bookingID giống như bookingID truyền xuống hay không
        Wallet customerWallet = customer.getWallet();
        //admin wallet
        Account admin = accountRepository.findByRole(Role.ADMIN).get(0);
        Wallet adminWallet = admin.getWallet();

        double refundAmount = booking.getTotalPrice();
        if(adminWallet.getBalance() < refundAmount){
            throw new BadRequestException("Admin has not enough money to refund");
        }
        customerWallet.setBalance(customerWallet.getBalance() + refundAmount);
        adminWallet.setBalance(adminWallet.getBalance() - refundAmount);
        //save the wallet
        walletRepository.save(customerWallet);
        walletRepository.save(adminWallet);

        //create a transaction to save
        Transaction transaction = new Transaction();
        transaction.setFrom(adminWallet);
        transaction.setTo(customerWallet);
        transaction.setAmount((float)refundAmount);
        transaction.setBooking(booking);
        transaction.setTransactionType(TransactionEnum.REFUND);
        transaction.setVenueId(booking.getVenueId());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        transaction.setTransactionDate(now.format(formatter));

        transactionRepository.save(transaction);

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepo.save(booking);
        return transaction;
    }

    public Booking confirmRefundFlexible(Booking booking, BookingDetail bookingDetail) {
        // Kiểm tra nếu chi tiết booking hợp lệ
        if (bookingDetail == null) {
            throw new BadRequestException("Chi tiết booking không hợp lệ");
        }

        // Xóa chi tiết booking
        bookingDetailService.deleteBookingDetail(bookingDetail.getId());

        // Hoàn lại thời gian cho booking
        booking.setRemainingTimes((int) (booking.getRemainingTimes() + bookingDetail.getDuration()));
        return bookingRepo.save(booking);
    }

    private boolean isValidCancellation(Booking booking) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bookingTime = booking.getApplicationDate().atStartOfDay();
        long hoursBetween = ChronoUnit.HOURS.between(now,bookingTime);

        switch (booking.getBookingType()) {
            case FIXED:
                return hoursBetween >= businessRuleConfig.getFixedCancelDays();
            case FLEXIBLE:
                return hoursBetween >= businessRuleConfig.getFlexibleCancelHours();
            case DAILY:
                return hoursBetween >= businessRuleConfig.getDailyCancelHours();
            default:
                return false;
        }
    }


    public Transaction processBookingComission(long bookingId) {
        Booking booking = bookingRepo.findBookingById(bookingId);
        CourtTimeSlot courtTimeSlot = booking.getBookingDetailList().get(0).getCourtTimeSlot();
        Venue venue = courtTimeSlot.getCourt().getVenue();
        Account manager = venue.getManager();

        Wallet managerWallet = walletRepository.findWalletByAccount_Id(manager.getId());
        Wallet adminWallet = walletRepository.findWalletByAccountRole(Role.ADMIN);

        float commissionRate = 0.1f;
        float commisssion = (float) booking.getTotalPrice() * commissionRate;
        float netAmount = (float) booking.getTotalPrice() - commisssion;

        if (adminWallet.getBalance() < booking.getTotalPrice()) {
            throw new BadRequestException("Admin does not have enought balance for the payment");
        }

        //Deduct from admin wallet
        adminWallet.setBalance(adminWallet.getBalance() - netAmount);
        walletRepository.save(adminWallet);

        //add net amount to court manager wallet
        managerWallet.setBalance(managerWallet.getBalance() + netAmount);
        walletRepository.save(managerWallet);

        //Create Transaction record for the transfer
        Transaction transferTransaction = new Transaction();
        transferTransaction.setAmount(netAmount);
        transferTransaction.setTransactionType(TransactionEnum.COMPLETED);
        transferTransaction.setFrom(adminWallet);
        transferTransaction.setTo(managerWallet);
        transferTransaction.setBooking(booking);
        transferTransaction.setDescription("Payment for court manager after commission");
        transferTransaction.setTransactionDate(LocalDateTime.now().toString());
        transactionRepository.save(transferTransaction);

        return transferTransaction;
    }

    public List<Booking> getBookingByUserId(long userId) {
        List<Booking> bookings = bookingRepo.findBookingByAccount_Id(userId);
        if (bookings.isEmpty()) {
            throw new NoDataFoundException("0 Booking History");
        }
        return bookings;
    }

    public Booking checkPaymentForBooking(long id){
        Booking booking = bookingRepo.findBookingById(id);
        List<Transaction> transactions = booking.getTransactions();
        if (transactions.isEmpty()){
            booking.setStatus(BookingStatus.CANCELLED);
            List<BookingDetail> bookingDetails = booking.getBookingDetailList();
            if (!bookingDetails.isEmpty()){
                for (BookingDetail bookingDetail : bookingDetails){
                    bookingDetail.getCourtTimeSlot().setStatus(SlotStatus.AVAILABLE);
                    courtTimeSlotRepo.save(bookingDetail.getCourtTimeSlot());
                }
            }
        }
        return bookingRepo.save(booking);
    }

    public List<BookingResponse> getBookingHistory() {
        Account user = authenticationService.getCurrentAccount();
        List<Booking> bookingList = bookingRepo.findBookingByAccount_Id(user.getId());
        if (bookingList.isEmpty()) {
            throw new BadRequestException("No Booking research");
        }
        List<BookingResponse> responseList = new ArrayList<>();
        for (Booking booking : bookingList) {
            BookingResponse response = mapToBookingResponse(booking);
            responseList.add(response);
        }
        return responseList;
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        BookingResponse bookingResponse = new BookingResponse();

        try {
            Venue venue = venueRepo.findVenueById(booking.getVenueId());
            if (venue == null) {
                throw new BadRequestException("Venue not found");
            }
            // Copy fields from Venue to BookingResponse
            bookingResponse.setBookingDate(booking.getBookingDate());
            bookingResponse.setBookingType(booking.getBookingType());
            bookingResponse.setTotalTimes(booking.getTotalTimes());
            bookingResponse.setTotalPrice(booking.getTotalPrice());
            bookingResponse.setApplicationDate(booking.getApplicationDate());
            bookingResponse.setRemainingTimes(booking.getRemainingTimes());
            bookingResponse.setStatus(booking.getStatus());
            bookingResponse.setId(booking.getId());
            bookingResponse.setAccountId(booking.getAccount().getId());
            int totalPurchasedTime = 0;
            List<BookingDetailResponse> detailResponses = new ArrayList<>();
            for (BookingDetail detail : booking.getBookingDetailList()){
                BookingDetailResponse bookingDetailResponse = new BookingDetailResponse();
                bookingDetailResponse.setDetailId(detail.getId());
                bookingDetailResponse.setCheckIndate(detail.getCourtTimeSlot().getCheckInDate());
                bookingDetailResponse.setCourtId(detail.getCourtTimeSlot().getCourt().getId());
                bookingDetailResponse.setTimeSlotId(detail.getCourtTimeSlot().getTimeSlot().getId());
                bookingDetailResponse.setDuration((int) detail.getDuration());
                detailResponses.add(bookingDetailResponse);

                totalPurchasedTime += detail.getDuration();
            }
            bookingResponse.setBookingDetails(detailResponses);
            // Additional fields for BookingResponse
            bookingResponse.setVenueId(venue.getId());
            bookingResponse.setVenueName(venue.getName());
            bookingResponse.setAccountName(booking.getAccount().getFullName());
            bookingResponse.setPurchasedTime(totalPurchasedTime);
            // Assume the price is calculated based on some business logic; here it's a placeholder
            return bookingResponse;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Venue getVenueFromBookingDetail(BookingDetail bookingDetail) {
        CourtTimeSlot courtTimeSlot = bookingDetail.getCourtTimeSlot();
        if (courtTimeSlot != null) {
            Court court = courtTimeSlot.getCourt();
            if (court != null) {
                return court.getVenue();
            }
        }
        return null;
    }

//    @Scheduled(fixedRate = 60000)
//    public List<BookingResponse> checkBookingForCancelllation(){
//        Account account = authenticationService.getCurrentAccount();
//        LocalDate oneDayAgo = LocalDate.now().minusDays(1);
//        List<Booking> bookings = bookingRepo.findByBookingDateBeforeAndStatusAndAccount_Id(oneDayAgo, BookingStatus.BOOKED, account.getId());
//        List<BookingResponse> bookingResponses = new ArrayList<>();
//        boolean isCancel = false;
//        for (Booking booking : bookings){
//            BookingResponse bookingResponse = mapToBookingResponse(booking);
//            bookingResponse.setCancel(true);
//            bookingResponses.add(bookingResponse);
//        }
//        return bookingResponses;
//    }


    public int getRemainingTimes(long bookingId) {
        Booking booking = bookingRepo.findBookingById(bookingId);
        if (booking != null) {
            return booking.getRemainingTimes();
        }
        throw new BadRequestException("Booking not found");
    }

    public List<Booking> getAllBookingsByAccount() {
        Account account = authenticationService.getCurrentAccount();
        List<Booking> bookings = bookingRepo.findBookingByAccount_Id(account.getId());
        return bookings;
    }

    public List<Map<String, Object>> getCourtRevenueData( int month, int year) {
        Account manager = authenticationService.getCurrentAccount();
        Venue venue = venueRepo.findVenueByManagerId(manager.getId());
        if(venue == null){
            throw new BadRequestException("This account is not manager");
        }
        List<Map<String, Object>> revenueData = new ArrayList<>();
        List<Object[]> courtRevenueResults = bookingDetailRepo.findRevenueByCourtAndBookingType(venue.getId(), month, year);
        for (Object[] result : courtRevenueResults) {
            Map<String, Object> courtData = new HashMap<>();
            courtData.put("name", result[0]);
            courtData.put("fixed", ((Number) result[1]).doubleValue());
            courtData.put("daily", ((Number) result[2]).doubleValue());
            courtData.put("flexible", ((Number) result[3]).doubleValue());

            revenueData.add(courtData);
        }

        return revenueData;
    }
    public List<BookingResponse> getBookedBooking( ){
        Account user = authenticationService.getCurrentAccount();
        List<Booking> bookings = bookingRepo.findByStatusAndAccount_Id(BookingStatus.BOOKED, user.getId());
        List<BookingResponse> responses = new ArrayList<>();
        for (Booking booking : bookings){
            BookingResponse response = mapToBookingResponse(booking);
            responses.add(response);
        }
        return responses;
    }



}
