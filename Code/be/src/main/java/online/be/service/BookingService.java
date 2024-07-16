package online.be.service;

import online.be.entity.*;
import online.be.enums.*;
import online.be.exception.BadRequestException;
import online.be.exception.DuplicateEntryException;
import online.be.exception.NoDataFoundException;
import online.be.model.Request.DailyScheduleBookingRequest;
import online.be.model.Request.FixedScheduleBookingRequest;
import online.be.model.Request.FlexibleBookingRequest;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.*;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepo;

    @Autowired
    BookingDetailRepostiory bookingDetailRepo;

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
    AccountRepostory accountRepostory;

    @Autowired
    CourtRepository courtRepo;


    @Autowired
    VenueRepository venueRepo;

    @Autowired
    EmailService emailService;

    public Booking createDailyScheduleBooking(DailyScheduleBookingRequest bookingRequest) {
        Account currentAccount = authenticationService.getCurrentAccount();
        LocalDate bookingDate = LocalDate.now();
        LocalDate checkInDate = LocalDate.parse(bookingRequest.getCheckInDate());

        // Retrieve and sort the timeslots based on startTime
        List<TimeSlot> timeSlots = bookingRequest.getTimeslot().stream().map(slotId -> timeSlotRepo.findById(slotId).orElseThrow(() -> new RuntimeException("Timeslot not found: " + slotId))).sorted(Comparator.comparing(TimeSlot::getStartTime)).collect(Collectors.toList());

        // Retrieve court by id
        Court court = courtRepo.findById(bookingRequest.getCourt()).orElseThrow(() -> new BadRequestException("Court not found with id: " + bookingRequest.getCourt()));

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
            booking.setStatus(BookingStatus.PENDING);
            booking.setTotalPrice(totalPrice);
            booking.setTotalTimes(totalHours);
            booking.setBookingDetailList(details);
            booking.setApplicationDate(checkInDate);
            return bookingRepo.save(booking);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException sqlException) {
                if (sqlException.getErrorCode() == 1062) {
                    throw new DuplicateEntryException("Duplicate entry detected: " + sqlException.getMessage());
                }
            }
            throw new RuntimeException("This slot is booked.");
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong, please try again!");
        }
    }
    public Booking createFixedScheduleBooking(FixedScheduleBookingRequest bookingRequest) {
        Account currentAccount = authenticationService.getCurrentAccount();
        LocalDate applicationStartDate = LocalDate.parse(bookingRequest.getApplicationStartDate());
        LocalDate bookingDate = LocalDate.now();

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

            Court court = courtRepo.findById(bookingRequest.getCourt()).orElseThrow(() ->
                    new BadRequestException("Court not found with id: " + bookingRequest.getCourt()));

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
            booking.setBookingType(BookingType.FIXED);
            booking.setStatus(BookingStatus.PENDING);
            booking.setApplicationDate(applicationStartDate);
            return bookingRepo.save(booking);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException sqlException) {
                if (sqlException.getErrorCode() == 1062) {
                    throw new DuplicateEntryException("Duplicate entry detected: " + sqlException.getMessage());
                }
            }
            throw new RuntimeException("This slot is booked.");
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong, please try again", e);
        }
    }
    public Booking checkIn(long id, LocalDate checkInDate) {
        Booking booking = bookingRepo.findById(id).orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        List<BookingDetail> details = booking.getBookingDetailList();
        int count = details.size();
        for (BookingDetail detail : details) {
            if (detail.getCourtTimeSlot().getCheckInDate().equals(checkInDate)) {
                CourtTimeSlot courtTimeSlot = courtTimeSlotRepo.findByBookingDetail(detail);
                courtTimeSlot.setStatus(SlotStatus.CHECKED);
                courtTimeSlotRepo.save(courtTimeSlot);
                count = count - 1;
                if (booking.getRemainingTimes() != 0) {
                    booking.setRemainingTimes(booking.getRemainingTimes() - 1);
                }
            }
        }
        if (count == 0) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepo.save(booking);
        }
        return booking;
    }

    public void deleteBooking(Long bookingId) {
        bookingRepo.deleteById(bookingId);
    }
    //Tự tạo hiển thị không có Booking

    //mua số giờ chơi cho loại lịch linh hoạt
    public Booking purchaseFlexibleHours(int totalHour, long venueId, LocalDate applicationDate) {
        // Load current user
        Account user = authenticationService.getCurrentAccount();

        // Validate total hour
        if (totalHour <= 0) {
            throw new IllegalArgumentException("Total hour must be positive");
        }

        Venue venue = venueRepo.findById(venueId).orElseThrow(() ->
                new BadRequestException("Venue not found")
        );

        // Retrieve pricing for flexible booking
        double flexiblePricePerHour = venue.getPricingList().stream()
                .filter(p -> p.getBookingType().equals(BookingType.FLEXIBLE))
                .mapToDouble(Pricing::getPricePerHour)
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Flexible pricing not found"));

        //calculate price
        double totalPrice = flexiblePricePerHour * totalHour;
        // Create booking entity
        Booking booking = new Booking();
        booking.setAccount(user);
        booking.setBookingType(BookingType.FLEXIBLE);
        booking.setBookingDate(LocalDate.now());
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalTimes(totalHour);
        booking.setRemainingTimes(totalHour);
        booking.setApplicationDate(applicationDate);
        booking.setTotalPrice(totalPrice);
        // Save booking
        try {
            return bookingRepo.save(booking);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create booking", ex);
        }
    }

    public Booking createFlexibleScheduleBooking(FlexibleBookingRequest request) {
        Account user = authenticationService.getCurrentAccount();
        Booking booking = bookingRepo.findBookingById(request.getBookingId());
        List<BookingDetail> details = new ArrayList<>();
        LocalDate applicationDate = booking.getApplicationDate();

        try {
            for (FlexibleBookingRequest.FlexibleTimeSlot flexibleTimeSlot : request.getFlexibleTimeSlots()) {
                LocalDate endOfMonth = booking.getApplicationDate().plusDays(29);
                List<TimeSlot> timeSlots = new ArrayList<>();
                for (Long timeslotId : flexibleTimeSlot.getTimeslot()) {
                    TimeSlot timeSlot = timeSlotRepo.findById(timeslotId).orElseThrow(() ->
                            new BadRequestException("Timeslot not found: " + timeslotId));
                    timeSlots.add(timeSlot);
                }

                Court court = courtRepo.findById(flexibleTimeSlot.getCourt()).orElseThrow(() -> new BadRequestException("Court not found with id: " + flexibleTimeSlot.getCourt()));
                LocalDate checkInDate = LocalDate.parse(flexibleTimeSlot.getCheckInDate());
                if (checkInDate.isAfter(endOfMonth)){
                    throw new RuntimeException("Check-in Date is over the month you bought");
                }
                for (TimeSlot slot : timeSlots) {
                    BookingDetail detail = detailService.createBookingDetail(BookingType.FLEXIBLE, checkInDate, court, slot);
                    detail.setBooking(booking);
                    details.add(detail);
                }
            }

            booking.setBookingDetailList(details);
            return bookingRepo.save(booking);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException sqlException) {
                if (sqlException.getErrorCode() == 1062) {
                    throw new DuplicateEntryException("Duplicate entry detected: " + sqlException.getMessage());
                }
            }
            throw new RuntimeException("Failed to save booking: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create flexible booking: " + e.getMessage(), e);
        }
    }


    public Transaction processBookingPayment(long bookingId, long venueId) {
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
            List<Account> adminList = accountRepostory.findByRole(Role.ADMIN);
            if(adminList.isEmpty()){
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
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepo.save(booking);

            // Create and save the transaction
            Venue venue = venueRepo.findVenueById(venueId);
            if(venue == null){
                throw new BadRequestException("Venue not found");
            }
            Transaction transaction = createTransaction(customerWallet, adminWallet, booking, amount);
            transactionRepository.save(transaction);

            // Send email notification
            sendPaymentConfirmationEmail(customer, amount, bookingId,venue);

            return transaction;
        } catch (Exception e) {
            // Rollback transaction if necessary and handle exception
            throw new RuntimeException("Error processing booking payment", e);
        }
    }

    private void updateBalances(Wallet customerWallet, Wallet adminWallet, double amount) {
        customerWallet.setBalance(customerWallet.getBalance() - (float) amount);
        adminWallet.setBalance(adminWallet.getBalance() + (float) amount);
        walletRepository.save(adminWallet);
        walletRepository.save(customerWallet);
    }

    private Transaction createTransaction(Wallet fromWallet, Wallet toWallet, Booking booking, double amount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.now().toString());
        transaction.setFrom(fromWallet);
        transaction.setTo(toWallet);
        transaction.setBooking(booking);
        transaction.setTransactionType(TransactionEnum.COMPLETED);
        transaction.setAmount((float) amount);
        transaction.setDescription("Pay for booking");
        return transaction;
    }

    private void sendPaymentConfirmationEmail(Account customer, double amount, long bookingId, Venue venue) {
        // Lấy danh sách chi tiết booking
        List<BookingDetail> bookingDetails = bookingDetailRepo.findByBookingId(bookingId);

        // Tạo chuỗi chi tiết booking
        StringBuilder bookingDetailsString = new StringBuilder();
        for (BookingDetail detail : bookingDetails) {
            bookingDetailsString.append(String.format("Date: %s, Time: %s - %s, Court: %s\n",
                    detail.getCourtTimeSlot().getCheckInDate(),
                    detail.getCourtTimeSlot().getTimeSlot().getStartTime(),
                    detail.getCourtTimeSlot().getTimeSlot().getEndTime(),
                    detail.getCourtTimeSlot().getCourt().getCourtName()));
        }

        // Tạo subject và description của email
        String subject = "Booking Payment Confirmation";
        String description = String.format(
                "Dear %s,\n\nYour payment of %.2f for booking ID %d has been successfully processed.\n\n" +
                        "Venue: %s\n" +
                        "Booking Details:\n%s\n" +
                        "Thank you for your booking!\n\n" +
                        "Best regards,\ngoodminton.online",
                customer.getFullName(), amount, bookingId, venue.getName(), bookingDetailsString.toString());

        // Gửi email
        emailService.sendMail(customer, subject, description);
    }

    //cancel booking
    public Booking cancelBooking(long bookingId) {
        Booking booking = bookingRepo.findBookingById(bookingId);
        //search user wallet
        Account customer = authenticationService.getCurrentAccount();//lấy  tài khoản hiện tại
        //kiểm tra xem customer này có bookingID giống như bookingID truyền xuống hay không
        Wallet customerWallet = customer.getWallet();
        //admin wallet
        Account admin = accountRepostory.findByRole(Role.ADMIN).get(0);
        Wallet adminWallet = admin.getWallet();
        if (booking != null) {
            if (isValidCancellation(booking)) {
                booking.setStatus(BookingStatus.CANCELLED);
                adminWallet.setBalance(adminWallet.getBalance() - (float) booking.getTotalPrice());
                customerWallet.setBalance(customerWallet.getBalance() + (float) booking.getTotalPrice());

                walletRepository.save(adminWallet);
                walletRepository.save(customerWallet);
                return bookingRepo.save(booking);
            } else {
                throw new BadRequestException("This booking cannot be cancelled");
            }
        } else {
            throw new BadRequestException("Booking not found");
        }
    }

    private boolean isValidCancellation(Booking booking) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate bookingDate = booking.getBookingDate();

        switch (booking.getBookingType()) {
            case FIXED:
            case FLEXIBLE:
                return ChronoUnit.DAYS.between(booking.getBookingDate(), now) >= 7;
            case DAILY:
                return ChronoUnit.MINUTES.between(booking.getBookingDate(), now) >= 30;
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

    public List<Booking> getBookingHistory(){
        Account user = authenticationService.getCurrentAccount();
        List<Booking> bookingList = bookingRepo.findBookingByAccount_Id(user.getId());
        if(bookingList.isEmpty()){
            throw new BadRequestException("No Booking research");
        }
        return bookingList;
    }
}
