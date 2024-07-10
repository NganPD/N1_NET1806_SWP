package online.be.service;

import online.be.entity.*;
import online.be.enums.*;
import online.be.exception.BadRequestException;
import online.be.exception.DuplicateEntryException;
import online.be.model.Request.DailyScheduleBookingRequest;
import online.be.model.Request.FixedScheduleBookingRequest;
import online.be.model.Request.FlexibleBookingRequest;
import online.be.repository.*;
import online.be.util.DateTimeUtils;
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
    DiscountRepository discountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountRepostory accountRepostory;

    @Autowired
    CourtRepository courtRepo;

    @Autowired
    DateTimeUtils utils;

    @Autowired
    VenueRepository venueRepo;

    public Booking createDailyScheduleBooking(DailyScheduleBookingRequest bookingRequest) {
        Account currentAccount = authenticationService.getCurrentAccount();
        LocalDate bookingDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate checkInDate = LocalDate.parse(bookingRequest.getCheckInDate(), formatter);

        // Retrieve and sort the timeslots based on startTime
        List<TimeSlot> timeSlots = bookingRequest.getTimeslot().stream()
                .map(slotId -> timeSlotRepo.findById(slotId)
                        .orElseThrow(() -> new RuntimeException("Timeslot not found: " + slotId)))
                .collect(Collectors.toList());

        // Retrieve court by id
        Court court = courtRepo.findById(bookingRequest.getCourt()).orElseThrow(() ->
                new BadRequestException("Court not found with id: " + bookingRequest.getCourt()));

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
                    court,
                    timeSlot
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
            throw new RuntimeException("This slot is booked.");
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong, please try again!");
        }
    }

    public Booking createFixedScheduleBooking(FixedScheduleBookingRequest bookingRequest) {
        Account currentAccount = authenticationService.getCurrentAccount();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate applicationStartDate = LocalDate.parse(bookingRequest.getApplicationStartDate(), formatter);
        LocalDate bookingDate = LocalDate.now();

        Booking booking = new Booking();
        List<BookingDetail> details = new ArrayList<>();

        for (FixedScheduleBookingRequest.FixedTimeSlot fixedTimeSlot : bookingRequest.getFixedTimeSlots()) {
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(fixedTimeSlot.getDayOfWeek().toUpperCase());

            // Retrieve and sort the timeslots based on startTime
            List<TimeSlot> timeSlots = fixedTimeSlot.getTimeslot().stream()
                    .map(slotId -> timeSlotRepo.findById(slotId)
                            .orElseThrow(() -> new RuntimeException("Timeslot not found: " + slotId)))
                    .collect(Collectors.toList());

            // Retrieve court by id
            Court court = courtRepo.findById(fixedTimeSlot.getCourt()).orElseThrow(() ->
                    new BadRequestException("Court not found with id: " + fixedTimeSlot.getCourt()));


            for (int month = 0; month < bookingRequest.getDurationInMonths(); month++) {
                LocalDate startOfMonth = applicationStartDate.plusDays(month * 30L);
                LocalDate endOfMonth = startOfMonth.plusDays(29); // 30 ngày từ ngày bắt đầu

                // Kiểm tra ngày đầu tiên trong tháng là ngày nào
                LocalDate firstBookingDay = startOfMonth.with(TemporalAdjusters.nextOrSame(dayOfWeek));

                for (LocalDate date = firstBookingDay;
                     !date.isAfter(endOfMonth);
                     date = date.plusWeeks(1)) {

                    for (TimeSlot slot : timeSlots) {
                        BookingDetail detail = detailService.createBookingDetail(
                                BookingType.FIXED,
                                date.format(formatter),
                                court,
                                slot
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

    public Booking checkIn(long id, LocalDate checkInDate) {
        Booking booking = bookingRepo.findById(id).orElseThrow(() ->
                new RuntimeException("Booking not found with id: " + id));
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
    public Booking purchaseFlexibleHours(int totalHour, long venueId) {
        // Load current user
        Account user = authenticationService.getCurrentAccount();

        // Validate total hour
        if (totalHour <= 0) {
            throw new IllegalArgumentException("Total hour must be positive");
        }

        // Create booking entity
        Booking booking = new Booking();
        booking.setAccount(user);
        booking.setBookingType(BookingType.FLEXIBLE);
        booking.setBookingDate(LocalDate.now());
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalTimes(totalHour);
        booking.setRemainingTimes(totalHour);
        Discount discount = new Discount();
        discount.setDiscount(0);
        if (totalHour >= 30) {
            discount.setDiscount(0.2);
        }
        discount.setDescription("Discount for flexible booking");
        // Save discount first
        discount = discountRepository.save(discount);

        // Set discount in booking
        booking.setDiscount(discount);

        double price = venueRepo.findVenueById(venueId).getTimeSlots().get(0).getPrice();
        booking.setTotalPrice(price*(1-discount.getDiscount()));
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

        try {
            for (FlexibleBookingRequest.FlexibleTimeSlot flexibleTimeSlot : request.getFlexibleTimeSlots()) {
                List<TimeSlot> timeSlots = flexibleTimeSlot.getTimeslot().stream()
                        .map(slotId -> timeSlotRepo.findById(slotId)
                                .orElseThrow(() -> new BadRequestException("Timeslot not found: " + slotId)))
                        .collect(Collectors.toList());

                Court court = courtRepo.findById(flexibleTimeSlot.getCourt()).orElseThrow(() ->
                        new BadRequestException("Court not found with id: " + flexibleTimeSlot.getCourt()));

                for (TimeSlot slot : timeSlots) {
                    BookingDetail detail = detailService.createBookingDetail(
                            BookingType.FLEXIBLE,
                            flexibleTimeSlot.getCheckInDate(),
                            court,
                            slot
                    );
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



    //payForBooking
//    public Transaction processBookingPayment(long bookingId) {
//        //search booking
//        Booking booking = bookingRepo.findBookingById(bookingId);
//        if (booking == null) {
//            throw new BadRequestException("Booking not found");
//        }
//        //search user wallet
//        Account customer = authenticationService.getCurrentAccount();
//        Wallet customerWallet = customer.getWallet();
//        //admin wallet
//        Account admin = accountRepostory.findAdmin();
//        Wallet adminWallet = admin.getWallet();
//        double amount = booking.getTotalPrice();
//
//        if (customerWallet.getBalance() >= amount) {
//            booking.setStatus(BookingStatus.CONFIRMED);
//            customerWallet.setBalance(customerWallet.getBalance() - (float) amount);
//            adminWallet.setBalance(adminWallet.getBalance() + (float) amount);
//
//            walletRepository.save(adminWallet);
//            walletRepository.save(customerWallet);
//            bookingRepo.save(booking);
//
//            Transaction transaction = new Transaction();
//            transaction.setTransactionDate(LocalDateTime.now().toString());
//            transaction.setFrom(customerWallet);
//            transaction.setTo(adminWallet);
//            transaction.setBooking(booking);
//            transaction.setVenueID(booking.getBookingDetailList().get(0).getCourtTimeSlot().getCourt().getVenue().getId());
//            transaction.setTransactionType(TransactionEnum.COMPLETED);
//            transaction.setAmount((float) booking.getTotalPrice());
//            transaction.setDescription("Pay for booking");
//            return transactionRepository.save(transaction);
//        } else {
//            throw new BadRequestException("Customer does not have enough balance.");
//        }
//    }
//
//    //cancel booking
//    public Booking cancelBooking(long bookingId) {
//        Booking booking = bookingRepo.findBookingById(bookingId);
//        //search user wallet
//        Account customer = booking.getAccount();
//        Wallet customerWallet = customer.getWallet();
//        //admin wallet
//        Account admin = accountRepostory.findAdmin();
//        Wallet adminWallet = admin.getWallet();
//        if (booking != null) {
//            if (isValidCancellation(booking)) {
//                booking.setStatus(BookingStatus.CANCELLED);
//                adminWallet.setBalance(adminWallet.getBalance() - (float) booking.getTotalPrice());
//                customerWallet.setBalance(customerWallet.getBalance() + (float) booking.getTotalPrice());
//
//                walletRepository.save(adminWallet);
//                walletRepository.save(customerWallet);
//                return bookingRepo.save(booking);
//            } else {
//                throw new BadRequestException("This booking cannot be cancelled");
//            }
//        } else {
//            throw new BadRequestException("Booking not found");
//        }
//    }
//
//    private boolean isValidCancellation(Booking booking) {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime bookingDate = booking.getBookingDate();
//
//        switch (booking.getBookingType()) {
//            case FIXED:
//            case FLEXIBLE:
//                return ChronoUnit.DAYS.between(booking.getBookingDate(), now) >= 7;
//            case DAILY:
//                return ChronoUnit.MINUTES.between(booking.getBookingDate(), now) >= 30;
//            default:
//                return false;
//        }
//    }
//
//    public Transaction processBookingComission(long bookingId) {
//        Booking booking = bookingRepo.findBookingById(bookingId);
//        CourtTimeSlot courtTimeSlot = booking.getBookingDetailList().get(0).getCourtTimeSlot();
//        Venue venue = courtTimeSlot.getCourt().getVenue();
//        Account manager = venue.getManager();
//
//        Wallet managerWallet = walletRepository.findWalletByAccount_Id(manager.getId());
//        Wallet adminWallet = walletRepository.findWalletByAccountRole(Role.ADMIN);
//
//        float commissionRate = 0.1f;
//        float commisssion = (float) booking.getTotalPrice() * commissionRate;
//        float netAmount = (float) booking.getTotalPrice() - commisssion;
//
//        if (adminWallet.getBalance() < booking.getTotalPrice()) {
//            throw new BadRequestException("Admin does not have enought balance for the payment");
//        }
//
//        //Deduct from admin wallet
//        adminWallet.setBalance(adminWallet.getBalance() - netAmount);
//        walletRepository.save(adminWallet);
//
//        //add net amount to court manager wallet
//        managerWallet.setBalance(managerWallet.getBalance() + netAmount);
//        walletRepository.save(managerWallet);
//
//        //Create Transaction record for the transfer
//        Transaction transferTransaction = new Transaction();
//        transferTransaction.setAmount(netAmount);
//        transferTransaction.setTransactionType(TransactionEnum.COMPLETED);
//        transferTransaction.setFrom(adminWallet);
//        transferTransaction.setTo(managerWallet);
//        transferTransaction.setBooking(booking);
//        transferTransaction.setDescription("Payment for court manager after commission");
//        transferTransaction.setTransactionDate(LocalDateTime.now().toString());
//        transactionRepository.save(transferTransaction);
//
//        return transferTransaction;
//    }
//
//    public List<Booking> getBookingHistory() {
//        Account customer = authenticationService.getCurrentAccount();
//        return bookingRepo.findBookingByAccount_Id(customer.getId());
//    }

}
