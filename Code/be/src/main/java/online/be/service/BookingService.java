package online.be.service;

import online.be.entity.*;
import online.be.enums.BookingStatus;
import online.be.enums.PaymentStatus;
import online.be.exception.BadRequestException;
import online.be.exception.BookingException;
import online.be.model.Request.CreateBookingRequest;
import online.be.model.Request.UpdateBookingRequest;
import online.be.model.Response.BookingResponse;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


//    public BookingResponse createBooking(CreateBookingRequest createBookingRequest) {
//        try {
//            //validate input data
//            //check court availability
//            List<TimeSlotPrice> timeSlotPrices = courtScheduleRepository.findByCourtIdAndDateAndStartTimeAndEndTimeAndAvailable(
//                    createBookingRequest.getCourtId(), createBookingRequest.getDate(), createBookingRequest.getStartTime(),
//                    createBookingRequest.getEndTime(), true);
//
//            if (timeSlotPrices.isEmpty()) {
//                throw new BookingException("Court is not available this time");
//            }
//
//            //check booking type
//            TimeSlotPrice timeSlotPrice = timeSlotPrices.get(0);
//            if (timeSlotPrice.getBookingType() != createBookingRequest.getBookingType()) {
//                throw new BadRequestException("Booking type is not allowed for this time slot");
//            }
//            double duration = Duration.between(createBookingRequest.getStartTime(), createBookingRequest.getEndTime()).toHours();
//            //price
//            double price = 100000;
//            //create the new booking entity
//            Booking booking = new Booking();
//            booking.setBookingType(createBookingRequest.getBookingType());
//            booking.setBookingDate(LocalDate.now());
//            booking.setPaymentStatus(PaymentStatus.PENDING);
//            booking.setStatus(BookingStatus.PENDING);
//            booking.setAccount(booking.getAccount());
//            booking.setTotalHours(duration);
//            booking.setTotalPrice(price);
//            booking = bookingRepo.save(booking);
//
//            //create the booking detail entity
//            BookingDetail bookingDetail = new BookingDetail();
//            bookingDetail.setPrice(price);
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
