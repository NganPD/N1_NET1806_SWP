package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Booking;
import online.be.model.Request.DailyScheduleBookingRequest;
import online.be.model.Request.FixedScheduleBookingRequest;
import online.be.model.Request.FlexibleBookingRequest;
import online.be.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;


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

    @PostMapping("/request-cancel")
    public ResponseEntity<?> cancelBooking(
            @RequestParam(required = false) Long bookingId,
            @RequestParam(required = false) Long bookingDetailId) {
        return ResponseEntity.ok(bookingService.requestCancelBooking(bookingId, bookingDetailId));
    }

//    @PostMapping("/{bookingId}/processComission")
//    public ResponseEntity processCommision(@PathVariable long bookingId){
//        return ResponseEntity.ok(bookingService.processBookingComission(bookingId));
//    }

//    @GetMapping("/booking-history/{accountId}")
//    public ResponseEntity getBookingHistoryByAccount(@PathVariable long accountId){
//        return ResponseEntity.ok(bookingService.getBookingByUserId(accountId));
//    }

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


    @GetMapping("/court/{courtId}/month/{month}/year/{year}")
    public ResponseEntity<Map<String, Object>> getRevenueData(
            @PathVariable Long courtId,
            @PathVariable int month,
            @PathVariable int year) {
        Map<String, Object> revenueData = bookingService.getRevenueData(courtId, month, year);
        if (revenueData != null && !revenueData.isEmpty()) {
            return new ResponseEntity<>(revenueData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/venue/{venueId}/month/{month}/year/{year}")
    public ResponseEntity<Map<String, Object>> getVenueRevenueData(
            @PathVariable Long venueId,
            @PathVariable int month,
            @PathVariable int year) {
        Map<String, Object> revenueData = bookingService.getVenueRevenueData(venueId, month, year);
        if (revenueData != null && !revenueData.isEmpty()) {
            return new ResponseEntity<>(revenueData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping("/{bookingId}/remaining-times")
    public int getRemainingTimes(@PathVariable("bookingId") long bookingId){
        return bookingService.getRemainingTimes(bookingId);
    }

}