package online.be.service;

import online.be.entity.Booking;
import online.be.model.Request.BookingRequest;
import online.be.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepo;


    public Booking createBooking(BookingRequest bookingRequest){
        Booking booking = new Booking();

        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setBookingType(bookingRequest.getBookingType());
        booking.setHours(bookingRequest.getHours());
        booking.setPrice(bookingRequest.getPrice());
        booking.setAccount(bookingRequest.getAccount());
        booking.setPayment(bookingRequest.getPayment());

        return bookingRepo.save(booking);
    }

    public Booking getBookingById(Long bookingId){
        Booking booking = bookingRepo.findById(bookingId).get();
        if(booking == null) {
            throw new RuntimeException("This booking ID does not exist");
        }else{
            return booking;
        }
    }

    public List<Booking> getAllBookings(){
        return bookingRepo.findAll();
    }

    public Booking updateBooking(BookingRequest bookingRequest, Long bookingId){
        Booking booking = getBookingById(bookingId);

        booking.setBookingDate(bookingRequest.getBookingDate());
        booking.setBookingType(bookingRequest.getBookingType());
        booking.setHours(bookingRequest.getHours());
        booking.setPrice(bookingRequest.getPrice());
        booking.setAccount(bookingRequest.getAccount());
        booking.setPayment(bookingRequest.getPayment());

        return bookingRepo.save(booking);
    }

    public void deleteBooking(Long bookingId){
        bookingRepo.deleteById(bookingId);
    }
}
