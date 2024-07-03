package online.be.service;

import online.be.entity.*;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;
import online.be.enums.PaymentStatus;
import online.be.exception.BadRequestException;
import online.be.exception.BookingException;
import online.be.model.Request.CreateBookingRequest;
import online.be.model.Request.DailyScheduleBookingRequest;
import online.be.model.Request.UpdateBookingRequest;
import online.be.model.Response.BookingResponse;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepo;

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    BookingDetailRepostiory bookingDetailRepostiory;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    CourtRepository courtRepository;

    @Autowired
    TimeSlotPriceRepository timeSlotPriceRepository;


//    public BookingResponse bookDailySchedule(DailyScheduleBookingRequest dailyScheduleBookingRequest){
//        //check whether user login or not
//        Account customer = authenticationRepository.findById(dailyScheduleBookingRequest.getAccountId())
//                .orElseThrow(()-> new BadRequestException("User has not authenticated yet"));
//        //check venue availability
//        List<Court> availableCourts = courtRepository.findAvailableCourtsForTimeSlot(dailyScheduleBookingRequest.getVenueId(),
//                dailyScheduleBookingRequest.getTimeSlotId());
//        if(availableCourts.isEmpty()){
//            throw new BookingException("No available courts for the selected timeslot");
//        }
//        //reference price
//        TimeSlotPrice timeSlotPrice = timeSlotPriceRepository.findByTimeSlotAndBookingType(dailyScheduleBookingRequest.getTimeSlotId(),
//                BookingType.ONE_DAY);
//        if (timeSlotPrice == null) {
//            throw new RuntimeException("No price found for the selected time slot and booking type.");
//        }
//        //Logic for booking daily schedule
//        //create a new booking entity
//        Booking booking = new Booking();
//        //set court
//        booking.setBookingDate(dailyScheduleBookingRequest.getBookingDate());
//        booking.setBookingType(BookingType.ONE_DAY);
//        booking.setStatus(BookingStatus.PENDING);
//        booking.setAccount(customer);
//        booking = bookingRepo.save(booking);
//        //create booking detail entity
//        BookingDetail bookingDetail = new BookingDetail();
//        bookingDetail.setBooking(booking);
//        bookingDetail.setDate(LocalDate.now());
//        bookingDetail.setPrice(timeSlotPrice.getPrice());
//        bookingDetail.setTimeSlot();
//    }

    public Booking getBookingById(long bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new BookingException("This booking ID does not exist"));
        return booking;
    }

    public List<Booking> getAllBooking() {
        return bookingRepo.findAll();
    }


    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }//Tự tạo hiển thị khi không có Booking

    public Booking updateBooking(UpdateBookingRequest updateBookingRequest, Long bookingId) {
        Booking booking = getBookingById(bookingId);


        return bookingRepo.save(booking);
    }
    //Chưa try-catch và xử lý lỗi không tồn tại

    public void deleteBooking(Long bookingId) {
        bookingRepo.deleteById(bookingId);
    }
    //Tự tạo hiển thị không có Booking
}
