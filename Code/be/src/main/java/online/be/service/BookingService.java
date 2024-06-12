package online.be.service;

import online.be.entity.Booking;
import online.be.enums.BookingStatus;
import online.be.model.Request.BookingRequest;
import online.be.model.Response.BookingResponse;
import online.be.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepo;


    public BookingResponse createBooking(BookingRequest bookingRequest){
        Booking booking = new Booking();

        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setBookingType(bookingRequest.getBookingType());
        booking.setHours(bookingRequest.getHours());
        booking.setPrice(bookingRequest.getPrice());
        booking.setAccount(bookingRequest.getAccount());
        booking.setPayment(bookingRequest.getPayment());
        bookingRepo.save(booking);
        //tra ve response
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getBookingId());
        response.setMessage("Booking created successfully");
        response.setBookingStatus(BookingStatus.SUCCESS);
        return response;
    }

    public BookingResponse getBookingById(Long bookingId){
        Booking booking = bookingRepo.findById(bookingId).orElse(null);
        BookingResponse response = new BookingResponse();
        if(booking != null){
            response.setBookingId(booking.getBookingId());
            response.setBookingStatus(BookingStatus.SUCCESS);
            response.setMessage("Booking found");
        }else{
            response.setMessage("Booking not found");
            response.setBookingStatus(BookingStatus.FAILURE);
        }
        return response;
    }

    public List<Booking> getAllBookings(){
        return bookingRepo.findAll();
    }

    public BookingResponse updateBooking(BookingRequest bookingRequest, Long bookingId){
        Booking existingBooking = bookingRepo.findById(bookingId).orElse(null);
        BookingResponse response = new BookingResponse();
        if(existingBooking != null) {
            //update existingBooking fields base on request
            existingBooking.setBookingDate(bookingRequest.getBookingDate());
            existingBooking.setBookingType(bookingRequest.getBookingType());
            existingBooking.setHours(bookingRequest.getHours());
            existingBooking.setPrice(bookingRequest.getPrice());
            existingBooking.setAccount(bookingRequest.getAccount());
            existingBooking.setPayment(bookingRequest.getPayment());
            bookingRepo.save(existingBooking);

            response.setBookingId(bookingId);
            response.setBookingStatus(BookingStatus.SUCCESS);
            response.setMessage("Updated Successfully");
        }else{
            response.setMessage("Booking not found");
            response.setBookingStatus(BookingStatus.FAILURE);
        }
        return response;
    }

    public void deleteBooking(Long bookingId){
        bookingRepo.deleteById(bookingId);
    }
}
