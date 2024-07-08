package online.be.service;

import jakarta.transaction.Transactional;
import online.be.entity.Account;
import online.be.entity.Booking;
import online.be.entity.BookingDetail;
import online.be.entity.TimeSlot;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;
import online.be.exception.DuplicateEntryException;
import online.be.model.Request.DailyScheduleBookingRequest;
import online.be.model.Request.FixedScheduleBookingRequest;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.*;

import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
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
    WalletRepository walletRepository;

    @Autowired
    CourtTimeSlotRepository courtTimeSlotRepo;
    TransactionRepository transactionRepository;

    @Autowired
    AuthenticationService authenticationService;

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
    public void deleteBooking(Long bookingId) {
        bookingRepo.deleteById(bookingId);
    }
    //Tự tạo hiển thị không có Booking

    //cancel booking
    public Transaction cancelBooking(long bookingId){
        Booking booking = bookingRepo.findBookingById(bookingId);
        LocalDate now = LocalDate.now();
        LocalDate bookingDate = booking.getBookingDate();

        boolean canCancel = false;

        switch (booking.getBookingType()){
            case FIXED:
            case FLEXIBLE:
                canCancel = now.isBefore(bookingDate.minusDays(7));
                break;
            case DAILY:
                canCancel = now.isBefore(bookingDate);
                break;
        }

        //if canCancel is true
        if(canCancel){
            //get adminWallet and userwallet
            Wallet adminWallet = walletRepository.findWalletByAccountRole(Role.ADMIN);
            Wallet userWallet = booking.getAccount().getWallet();

            float refundAmount = (float)booking.getTotalPrice();

            //update wallet balances
            adminWallet.setBalance(adminWallet.getBalance()-refundAmount);
            userWallet.setBalance(userWallet.getBalance() + refundAmount);
            walletRepository.save(adminWallet);
            walletRepository.save(userWallet);

            //create refund transaction
            Transaction refundTransaction = new Transaction();
            refundTransaction.setAmount(refundAmount);
            refundTransaction.setTransactionType(TransactionEnum.REFUND);
            refundTransaction.setFrom(adminWallet);
            refundTransaction.setTo(userWallet);
            refundTransaction.setBooking(booking);
            refundTransaction.setDescription("Refund for booking cancellation");
            refundTransaction.setTransactionDate(LocalDateTime.now().toString());
            transactionRepository.save(refundTransaction);

            //update booking status to cancel
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepo.save(booking);

            return refundTransaction;
        }else{
            //set the msg to customer that we cannot refund user's money
            throw new RuntimeException("Cancellation aborted by customer");
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

}
