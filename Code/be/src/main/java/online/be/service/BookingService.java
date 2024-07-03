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


//    public BookingResponse createBooking(CreateBookingRequest createBookingRequest) {
//        try {
//            //validate input data
//            //check court availability
//            List<TimeSlot> timeSlots = timeSlotRepository.findAvailableTimeSlotsByCourtIdAndDate(
//                    createBookingRequest.getCourtId(), createBookingRequest.getDate(), createBookingRequest.getStartTime(),
//            createBookingRequest.getEndTime());
//            if(timeSlots.isEmpty()){
//                throw new BookingException("Court is not available at this time");
//            }
//            //check booking type and price
//
//            //check account
//            Account account = authenticationRepository.findById(createBookingRequest.getUserId())
//                    .orElseThrow(()-> new BookingException("Account not found"));
//            //create the new booking entity
//            int totalHours = createBookingRequest.getEndTime().getHour() - createBookingRequest.getStartTime().getHour();
//            int totalMinutes = createBookingRequest.getEndTime().getMinute() - createBookingRequest.getStartTime().getMinute();
//            double totalTimes = totalHours + totalMinutes;
//            Booking booking = new Booking();
//            booking.setBookingType(createBookingRequest.getBookingType());
//            booking.setBookingDate(LocalDate.now());
//            booking.setPaymentStatus(PaymentStatus.PENDING);
//            booking.setStatus(BookingStatus.PENDING);
//            booking.setAccount(booking.getAccount());
//            booking.setTotalTimes(totalTimes);
//            booking.setTotalPrice();
//            booking = bookingRepo.save(booking);
//
//            //create the booking detail entity
//            BookingDetail bookingDetail = new BookingDetail();
//            bookingDetail.setPrice(bookingDetail.getPrice());
//            bookingDetail.setDate(createBookingRequest.getDate());
//            bookingDetail.setStatus(BookingStatus.PENDING);
//            bookingDetail.setBooking(booking);
//            bookingDetail.setTimeSlotPrice(timeSlotPrice);
//
//            bookingDetailRepostiory.save(bookingDetail);
//
//            //generate payment URL
//            String paymentUrl =
//        }
//        //Sửa lại createBooking theo luồng yêu cầu của FunctionalRequirement
//        //Nên dùng try catch khi cố tạo một đối tượng mới để handle lỗi
//    }

    public BookingResponse bookDailySchedule(DailyScheduleBookingRequest dailyScheduleBookingRequest){
        //check whether user login or not
        Account customer = authenticationRepository.findById(dailyScheduleBookingRequest.getAccountId())
                .orElseThrow(()-> new BadRequestException("User has not authenticated yet"));
        //check venue availability
        List<Court> availableCourts = courtRepository.findAvailableCourtsForTimeSlot(dailyScheduleBookingRequest.getVenueId(),
                dailyScheduleBookingRequest.getTimeSlotId());
        if(availableCourts.isEmpty()){
            throw new BookingException("No available courts for the selected timeslot");
        }
        //reference price
        TimeSlotPrice timeSlotPrice = timeSlotPriceRepository.findByTimeSlotAndBookingType(dailyScheduleBookingRequest.getTimeSlotId(),
                BookingType.ONE_DAY);
        //Logic for booking daily schedule
        //create a new booking entity
        Booking booking = new Booking();
        //set court
        booking.setBookingDate(dailyScheduleBookingRequest.getBookingDate());
        booking.setBookingType(BookingType.ONE_DAY);
        booking.setStatus(BookingStatus.PENDING);
        booking.setAccount(customer);
        booking = bookingRepo.save(booking);
        //create booking detail entity
        BookingDetail bookingDetail = new BookingDetail();
        bookingDetail.setBooking(booking);
        bookingDetail.setPrice(timeSlotPrice.getPrice());
    }

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
