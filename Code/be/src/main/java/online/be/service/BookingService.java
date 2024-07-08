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

    @Autowired
    CourtTimeSlotRepository courtTimeSlotRepo;

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
}
