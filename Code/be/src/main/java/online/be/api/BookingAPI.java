package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Booking;
import online.be.model.Request.DailyScheduleBookingRequest;
import online.be.model.Request.FixedScheduleBookingRequest;
import online.be.model.Request.FlexibleBookingRequest;
import online.be.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/booking")
@SecurityRequirement(name = "api")
public class BookingAPI {
    @Autowired
    BookingService bookingService;

    @PostMapping("/flexible")
    public ResponseEntity createFlexibleBooking(@RequestBody FlexibleBookingRequest bookingRequest){
        Booking createdBooking = bookingService.createFlexibleScheduleBooking(bookingRequest);
        return ResponseEntity.ok(createdBooking);
    }
//
//    @PostMapping("/payment/{bookingId}/{venueId}")
//    public ResponseEntity payForBooking(@RequestParam long bookingId, @PathVariable long venueId){
//        return ResponseEntity.ok(bookingService.processBookingPayment(bookingId, venueId));
//    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity cancelBooking(@PathVariable long bookingId){
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

//    @PostMapping("/{bookingId}/processComission")
//    public ResponseEntity processCommision(@PathVariable long bookingId){
//        return ResponseEntity.ok(bookingService.processBookingComission(bookingId));
//    }

    @GetMapping("/booking-history/{accountId}")
    public ResponseEntity getBookingHistoryByAccount(@PathVariable long accountId){
        return ResponseEntity.ok(bookingService.getBookingByUserId(accountId));
    }

    @GetMapping("/booking-history")
    public ResponseEntity getBookingHistory(){
        return ResponseEntity.ok(bookingService.getBookingHistory());
    }

//        @GetMapping("/{bookingId}")
//    public ResponseEntity<Booking> getBookingById(@PathVariable long bookingId){
//        Booking Booking = bookingService.getBookingById(bookingId);
//        return ResponseEntity.ok().body(Booking);
//
//
//    @GetMapping
//    public ResponseEntity<List<Booking>> getAllBookings(){
//        List<Booking> Bookings = bookingService.getAllBookings();
//        return ResponseEntity.ok().body(Bookings);
//    }
//
//    @GetMapping("/availability")
//    public ResponseEntity getAvailableTimeSlots(@RequestParam BookingType bookingType){
//        List<TimeSlotAvabilityResponse> avabilityResponses = timeSlotPriceService.getAvailableTimeSlotForBookingType(bookingType);
//        return ResponseEntity.ok(avabilityResponses);
//    }
    @PostMapping("/daily-schedule")
    public ResponseEntity<Booking> createDailyScheduleBooking(@RequestBody DailyScheduleBookingRequest bookingRequest){
        Booking booking = bookingService.createDailyScheduleBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/fixed-schedule")
    public ResponseEntity<Booking> createFixedScheduleBooking(@RequestBody FixedScheduleBookingRequest bookingRequest){
        Booking booking = bookingService.createFixedScheduleBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @PatchMapping("/bookingId/{bookingId}/checkInDate/{checkInDate}")
    public ResponseEntity checkIn(@PathVariable long bookingId, @PathVariable String checkInDate){
        Booking booking = bookingService.checkIn(bookingId, checkInDate);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/purchase-hours/hours/{hours}/id/{id}/applicationDate/{applicationDate}")
    public ResponseEntity<Booking> purchaseFlexibleHours(@PathVariable int hours, @PathVariable long id, @PathVariable String applicationDate){
        Booking booking = bookingService.purchaseFlexibleHours(hours, id, applicationDate);
        return ResponseEntity.ok(booking);
    }
}