package online.be.service;

import online.be.entity.Account;
import online.be.entity.Booking;
import online.be.entity.BookingDetail;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;
import online.be.model.Request.DailyScheduleBookingRequest;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    TimeSlotPriceRepository timeSlotPriceRepo;

    @Autowired
    AuthenticationService authenticationService;

    public Booking createDailyScheduleBooking(DailyScheduleBookingRequest bookingRequest) {
        Account currentAccount = authenticationService.getCurrentAccount();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate bookingDate = LocalDate.parse(bookingRequest.getBookingDate(), formatter);
        Booking booking = new Booking();
        BookingDetail detail = detailService.createBookingDetail(bookingRequest.getBookingDetailRequests());
        detail.setBooking(booking);
        List<BookingDetail> details = new ArrayList<>();
        details.add(detail);
        int totalHours = 0;
        double totalPrice = 0;
        for (BookingDetail bookingDetail : details) {
            totalPrice += bookingDetail.getPrice();
            totalHours += (int) bookingDetail.getDuration();
        }
        try {
            booking.setAccount(currentAccount);
            booking.setBookingDate(bookingDate);
            booking.setTotalPrice(totalPrice);
            booking.setTotalTimes(totalHours);
            booking.setBookingDetailList(details);
            booking.setBookingType(BookingType.DAILY);
            booking.setStatus(BookingStatus.PENDING);
            return bookingRepo.save(booking);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong, please try again");
        }
    }
}
