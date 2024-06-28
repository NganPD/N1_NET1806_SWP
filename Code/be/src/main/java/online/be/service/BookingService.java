package online.be.service;

import online.be.entity.Account;
import online.be.entity.Booking;
import online.be.entity.BookingDetail;
import online.be.entity.CourtSchedule;
import online.be.enums.BookingStatus;
import online.be.enums.PaymentStatus;
import online.be.exception.BadRequestException;
import online.be.exception.BookingException;
import online.be.model.Request.BookingDetailRequest;
import online.be.model.Request.BookingRequest;
import online.be.model.Request.UpdateBookingRequest;
import online.be.model.Response.BookingResponse;
import online.be.repository.AuthenticationRepository;
import online.be.repository.BookingRepository;
import online.be.repository.CourtScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepo;

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    CourtScheduleRepository courtScheduleRepository;


    public Booking createBooking(BookingRequest bookingRequest){
        //kiem tra id nguoi dung
        Account account = authenticationRepository.findById(bookingRequest.getAccountId())
                .orElseThrow(() -> new BadRequestException("Account not found"));
        Booking booking = new Booking();
        booking.setAccount(account);
        booking.setBookingType(bookingRequest.getBookingType());
        booking.setBookingDate(LocalDate.now());
        booking.setStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.UNPAID);

        List<BookingDetail> bookingDetails = new ArrayList<>();
        for (BookingDetailRequest request : bookingRequest.getBookingDetailRequests()){
            BookingDetail detail = new BookingDetail();
            detail.setBooking(booking);
            detail.setCourtSchedule(courtScheduleRepository.findById(request.getScheduleId())
                    .orElseThrow(()-> new BadRequestException("Schedule not found")));
            bookingDetails.add(detail);
        }
        booking.setBookingDetailList(bookingDetails);
        return bookingRepo.save(booking);
    }
    //Sửa lại createBooking theo luồng yêu cầu của FunctionalRequirement
    //Nên dùng try catch khi cố tạo một đối tượng mới để handle lỗi

    public Booking getBookingById(long bookingId){
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(()-> new BookingException("This booking ID does not exist"));
            return booking;
    }

    public List<Booking> getAllBooking(){
        return bookingRepo.findAll();
    }



    public List<Booking> getAllBookings(){
        return bookingRepo.findAll();
    }//Tự tạo hiển thị khi không có Booking

    public Booking updateBooking(UpdateBookingRequest updateBookingRequest, Long bookingId){
        Booking booking = getBookingById(bookingId);



        return bookingRepo.save(booking);
    }
    //Chưa try-catch và xử lý lỗi không tồn tại

    public void deleteBooking(Long bookingId){
        bookingRepo.deleteById(bookingId);
    }
    //Tự tạo hiển thị không có Booking
}
