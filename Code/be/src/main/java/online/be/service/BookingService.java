package online.be.service;

import jakarta.transaction.Transactional;
import online.be.entity.*;
import online.be.enums.*;
import online.be.exception.BadRequestException;
import online.be.exception.BookingException;
import online.be.exception.DuplicateEntryException;
import online.be.model.Request.DailyScheduleBookingRequest;
import online.be.model.Request.FixedScheduleBookingRequest;
import online.be.model.Request.FlexibleBookingRequest;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.*;

import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    public Booking createDailyScheduleBooking(DailyScheduleBookingRequest bookingRequest) {
        Account currentAccount = authenticationService.getCurrentAccount();
        LocalDateTime bookingDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate checkInDate = LocalDate.parse(bookingRequest.getCheckInDate(),formatter);

        // Verify that there is at least one timeslot in the request
        if (bookingRequest.getTimeslot().isEmpty()) {
            throw new RuntimeException("No timeslots provided!");
        }

        // Retrieve and sort the timeslots based on startTime
        List<TimeSlot> timeSlots = bookingRequest.getTimeslot().stream()
                .map(slotId -> timeSlotRepo.findById(slotId)
                        .orElseThrow(() -> new RuntimeException("Timeslot not found: " + slotId)))
                .sorted(Comparator.comparing(TimeSlot::getStartTime))
                .collect(Collectors.toList());

        // Get the start time of the earliest timeslot
        LocalTime earliestStartTime = timeSlots.get(0).getStartTime();

        // Validate the check-in date
        if (checkInDate.isBefore(bookingDate.toLocalDate())) {
            throw new RuntimeException("Check-in date is invalid!");
        } else if (checkInDate.equals(bookingDate.toLocalDate())) {
            LocalDateTime earliestStartDateTime = LocalDateTime.of(checkInDate, earliestStartTime);
            if (Duration.between(bookingDate, earliestStartDateTime).toMinutes() < 30) {
                throw new RuntimeException("Booking must be made at least 30 minutes before the start time!");
            }
        }

        // Create booking
        Booking booking = new Booking();
        List<BookingDetail> details = new ArrayList<>();
        double totalPrice = 0;
        int totalHours = 0;

        // Create a booking detail for each timeslot
        for (TimeSlot timeSlot : timeSlots) {
            BookingDetail detail = detailService.createBookingDetail(
                    BookingType.DAILY,
                    bookingRequest.getCheckInDate(),
                    bookingRequest.getCourt(),
                    timeSlot.getId()
            );
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
            return bookingRepo.save(booking);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException sqlException) {
                if (sqlException.getErrorCode() == 1062) {
                    throw new DuplicateEntryException("Duplicate entry detected: " + sqlException.getMessage());
                }
            }
            throw new RuntimeException("Something went wrong, please try again", e);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong, please try again");
        }
    }

    @Transactional
    public Booking createFixedScheduleBooking(FixedScheduleBookingRequest bookingRequest) {
        Account currentAccount = authenticationService.getCurrentAccount();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate applicationStartDate = LocalDate.parse(bookingRequest.getApplicationStartDate(), formatter);
        LocalDateTime bookingDate = LocalDateTime.now();

        // Kiểm tra nếu tháng của bookingDate là sau tháng của applicationStartDate
        if (bookingDate.getYear() > applicationStartDate.getYear() ||
                (bookingDate.getYear() == applicationStartDate.getYear() && bookingDate.getMonthValue() >= applicationStartDate.getMonthValue())) {
            throw new IllegalArgumentException("Booking date month must be before application start date month.");
        }

        // Kiểm tra nếu bookingDate là ngày cuối tháng và applicationStartDate cách bookingDate ít nhất 7 ngày
        int dayOfMonth = bookingDate.getDayOfMonth();
        if (dayOfMonth >= 25 && dayOfMonth <= 31) {
            if (applicationStartDate.isBefore(ChronoLocalDate.from(bookingDate.plusDays(7)))) {
                throw new IllegalArgumentException("For booking dates in the last days of the month, application start date must be at least 7 days after the booking date.");
            }
        }

        Booking booking = new Booking();
        List<BookingDetail> details = new ArrayList<>();

        for (FixedScheduleBookingRequest.FixedTimeSlot fixedTimeSlot : bookingRequest.getFixedTimeSlots()) {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(fixedTimeSlot.getDayOfWeek().toUpperCase());

            for (int month = 0; month < bookingRequest.getDurationInMonths(); month++) {
                LocalDate startOfMonth = applicationStartDate.plusDays(month *  30L);
                LocalDate endOfMonth = startOfMonth.plusDays(29); // 30 ngày từ ngày bắt đầu

                // Kiểm tra ngày đầu tiên trong tháng là ngày nào
                LocalDate firstBookingDay = startOfMonth.with(TemporalAdjusters.nextOrSame(dayOfWeek));

                for (LocalDate date = firstBookingDay;
                     !date.isAfter(endOfMonth);
                     date = date.plusWeeks(1)) {

                    for (Long slotId : fixedTimeSlot.getTimeslot()) {
                        BookingDetail detail = detailService.createBookingDetail(
                                BookingType.FIXED,
                                date.format(formatter),
                                fixedTimeSlot.getCourt(),
                                slotId
                        );
                        detail.setBooking(booking);
                        details.add(detail);
                    }
                }
            }
        }

        double totalPrice = details.stream().mapToDouble(BookingDetail::getPrice).sum();
        int totalHours = details.stream().mapToInt(detail -> (int) detail.getDuration()).sum();

        try {
            booking.setAccount(currentAccount);
            booking.setBookingDate(bookingDate);
            booking.setTotalPrice(totalPrice);
            booking.setTotalTimes(totalHours);
            booking.setBookingDetailList(details);
            booking.setBookingType(BookingType.FIXED);
            booking.setStatus(BookingStatus.PENDING);
            return bookingRepo.save(booking);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException sqlException) {
                if (sqlException.getErrorCode() == 1062) {
                    throw new DuplicateEntryException("Duplicate entry detected: " + sqlException.getMessage());
                }
            }
            throw new RuntimeException("Something went wrong, please try again", e);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong, please try again", e);
        }
    }

    public Booking checkIn(long id, LocalDate checkInDate){
        Booking booking = bookingRepo.findById(id).orElseThrow(() ->
                new RuntimeException("Booking not found with id: " + id));
        List<BookingDetail> details = booking.getBookingDetailList();
        int count = details.size();
        for (BookingDetail detail : details){
            if (detail.getCourtTimeSlot().getCheckInDate().equals(checkInDate)){
                CourtTimeSlot courtTimeSlot = courtTimeSlotRepo.findByBookingDetail(detail);
                courtTimeSlot.setStatus(SlotStatus.CHECKED);
                courtTimeSlotRepo.save(courtTimeSlot);
                count = count - 1;
                if (booking.getRemainingTimes() != 0){
                    booking.setRemainingTimes(booking.getRemainingTimes()-1);
                }
            }
        }
        if (count == 0){
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
    public Booking purchaseFlexibleHours(int totalHour, long venueId){
        Account user = authenticationService.getCurrentAccount();
        LocalDateTime purchaseDate = LocalDateTime.now();
        //validate total hour
        //create booking entity for purchasing hours
        Booking booking = new Booking();
        booking.setAccount(user);
        booking.setBookingType(BookingType.FLEXIBLE);
        booking.setBookingDate(purchaseDate);
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalTimes(totalHour);
        booking.setRemainingTimes(totalHour);

        //calculate total price base on hourly rate
        TimeSlot timeSlot = timeSlotRepo.findByVenueId(venueId);

    }
    public Booking createFlexibleScheduleBooking(FlexibleBookingRequest request){
        Account user = authenticationService.getCurrentAccount();
        LocalDateTime bookingDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate checkInDate = LocalDate.parse(request.getCheckInDate(), formatter);
        //kieemr tra so gio toi da va ngay hien tai
        if(request.getTotalHours() > 20){
            throw new BadRequestException("Total play hours cannot exceed 20 hours");
        }

        //lấy ngày bắt đầu slot sau đó cộng thêm 30 ngày
        LocalDate endDate = checkInDate.plusDays(30);
        if(checkInDate.isBefore(bookingDate.toLocalDate())){
            throw new BadRequestException("Start date cannot be in the past");
        }
        //Lay cac timeslot the ID va sap xep
        List<TimeSlot> timeSlots = request.getSelectedTimeSlotsId().stream()
                .map(slotId -> timeSlotRepo.findById(slotId)
                        .orElseThrow(() -> new RuntimeException("Timeslot not found: " + slotId)))
                .sorted(Comparator.comparing(TimeSlot::getStartTime))
                .collect(Collectors.toList());

        //kiem tra tinh hop le cua cac timeslot
        double totalPrice = 0;
        int totalDuration = 0;

        List<BookingDetail> details = new ArrayList<>();
        for (TimeSlot timeSlot : timeSlots){
            BookingDetail detail = detailService.createBookingDetail(
                    BookingType.FLEXIBLE,
                    request.getCheckInDate(),
                    request.getCourtId(),
                    timeSlot.getId()
            );
            totalPrice += detail.getPrice();
            totalDuration += (int) detail.getDuration();
            details.add(detail);
        }
        //kiem tra tong thoi gian dat lich
        if(totalDuration > request.getTotalHours()){
            throw new BadRequestException("Selected timeslots exceed the total play hours");
        }

        //create booking
        Booking booking = new Booking();
        booking.setAccount(user);
        booking.setBookingDate(bookingDate);
        booking.setBookingType(BookingType.FLEXIBLE);
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(totalPrice);
        booking.setTotalTimes(totalDuration);
        booking.setRemainingTimes(totalDuration);
        booking.setBookingDetailList(details);

        try {
            return bookingRepo.save(booking);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException sqlException) {
                if (sqlException.getErrorCode() == 1062) {
                    throw new DuplicateEntryException("Duplicate entry detected: " + sqlException.getMessage());
                }
            }
            throw new RuntimeException("Something went wrong, please try again", e);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong, please try again");
        }
    }


    //payForBooking
    public Transaction processBookingPayment(long bookingId){
        //search booking
        Booking booking = bookingRepo.findBookingById(bookingId);
        if(booking == null){
            throw new BadRequestException("Booking not found");
        }
        //search user wallet
        Account customer = authenticationService.getCurrentAccount();
        Wallet customerWallet = customer.getWallet();
        //admin wallet
        Account admin = accountRepostory.findAdmin();
        Wallet adminWallet = admin.getWallet();
        double amount = booking.getTotalPrice();

        if(customerWallet.getBalance() >= amount){
            booking.setStatus(BookingStatus.CONFIRMED);
            customerWallet.setBalance(customerWallet.getBalance() - (float)amount);
            adminWallet.setBalance(adminWallet.getBalance() + (float)amount);

            walletRepository.save(adminWallet);
            walletRepository.save(customerWallet);
            bookingRepo.save(booking);

            Transaction transaction = new Transaction();
            transaction.setTransactionDate(LocalDateTime.now().toString());
            transaction.setFrom(customerWallet);
            transaction.setTo(adminWallet);
            transaction.setBooking(booking);
            transaction.setVenueID(booking.getBookingDetailList().get(0).getCourtTimeSlot().getCourt().getVenue().getId());
            transaction.setTransactionType(TransactionEnum.COMPLETED);
            transaction.setAmount((float)booking.getTotalPrice());
            transaction.setDescription("Pay for booking");
            return transactionRepository.save(transaction);
        }else{
            throw new BadRequestException("Customer does not have enough balance.");
        }
    }
    //cancel booking
    public Booking cancelBooking(long bookingId){
        Booking booking = bookingRepo.findBookingById(bookingId);
        //search user wallet
        Account customer = booking.getAccount();
        Wallet customerWallet = customer.getWallet();
        //admin wallet
        Account admin = accountRepostory.findAdmin();
        Wallet adminWallet = admin.getWallet();
        if(booking != null){
            if(isValidCancellation(booking)){
                booking.setStatus(BookingStatus.CANCELLED);
                adminWallet.setBalance(adminWallet.getBalance() - (float)booking.getTotalPrice());
                customerWallet.setBalance(customerWallet.getBalance() + (float)booking.getTotalPrice());

                walletRepository.save(adminWallet);
                walletRepository.save(customerWallet);
                return bookingRepo.save(booking);
            }else{
                throw new BadRequestException("This booking cannot be cancelled");
            }
        }else{
            throw new BadRequestException("Booking not found");
        }
    }

    private boolean isValidCancellation(Booking booking){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bookingDate = booking.getBookingDate();

        switch (booking.getBookingType()){
            case FIXED:
            case FLEXIBLE:
                return ChronoUnit.DAYS.between(booking.getBookingDate(), now) >= 7;
            case DAILY:
                return ChronoUnit.MINUTES.between(booking.getBookingDate(), now) >= 30;
            default:
                return false;
        }
    }
    public Transaction processBookingComission(long bookingId){
        Booking booking = bookingRepo.findBookingById(bookingId);
        CourtTimeSlot courtTimeSlot = booking.getBookingDetailList().get(0).getCourtTimeSlot();
        Venue venue = courtTimeSlot.getCourt().getVenue();
        Account manager = venue.getManager();

        Wallet managerWallet = walletRepository.findWalletByAccount_Id(manager.getId());
        Wallet adminWallet = walletRepository.findWalletByAccountRole(Role.ADMIN);

        float commissionRate = 0.1f;
        float commisssion = (float) booking.getTotalPrice()*commissionRate;
        float netAmount = (float) booking.getTotalPrice() - commisssion;

        if(adminWallet.getBalance() < booking.getTotalPrice()){
            throw new BadRequestException("Admin does not have enought balance for the payment");
        }

        //Deduct from admin wallet
        adminWallet.setBalance(adminWallet.getBalance() - netAmount);
        walletRepository.save(adminWallet);

        //add net amount to court manager wallet
        managerWallet.setBalance(managerWallet.getBalance() + netAmount);
        walletRepository.save(managerWallet);

        //Create Transaction record for the transfer
        Transaction transferTransaction  = new Transaction();
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

    public List<Booking> getBookingHistory(){
        Account customer = authenticationService.getCurrentAccount();
        return bookingRepo.findBookingByAccount_Id(customer.getId());
    }

}
