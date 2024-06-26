//package online.be.api;
//
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import online.be.entity.Booking;
//import online.be.model.Request.BookingRequest;
//import online.be.service.BookingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("api/booking")
//@SecurityRequirement(name = "api")
//public class BookingAPI {
//    @Autowired
//    BookingService bookingService;
//
//    @PostMapping
//    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest){
//        Booking createdBooking = bookingService.createBooking(bookingRequest);
//        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{bookingId}")
//    public ResponseEntity<Booking> getBookingById(@PathVariable long bookingId){
//        Booking Booking = bookingService.getBookingById(bookingId);
//        return ResponseEntity.ok().body(Booking);
//    }
//
//    @PutMapping("/{bookingId}")
//    public ResponseEntity<Booking> updateBooking(@PathVariable long bookingId, @RequestBody BookingRequest bookingRequest){
//        Booking updatedBooking = bookingService.updateBooking(bookingRequest, bookingId);
//        return ResponseEntity.ok().body(updatedBooking);
//    }
//
//    @DeleteMapping("/{bookingId}")
//    public ResponseEntity<Void> deleteBooking(@PathVariable long bookingId){
//        bookingService.deleteBooking(bookingId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Booking>> getAllBookings(){
//        List<Booking> Bookings = bookingService.getAllBookings();
//        return ResponseEntity.ok().body(Bookings);
//    }
//}
