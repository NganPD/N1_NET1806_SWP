package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.TimeSlot;
import online.be.model.Request.TimeSlotRequest;
import online.be.model.Response.TimeSlotResponse;
import online.be.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/timeslots")
@SecurityRequirement(name = "api")
public class TimeSlotAPI {

    @Autowired
    private TimeSlotService timeSlotService;

    // Lấy tất cả các TimeSlot
    @GetMapping
    public ResponseEntity<List<TimeSlot>> getAllTimeSlots() {
        List<TimeSlot> timeSlotList = timeSlotService.getAllTimeSlots();
        return ResponseEntity.ok().body(timeSlotList);
    }

    // Lấy TimeSlot theo ID
    @GetMapping("/{timeSlotId}")
    public ResponseEntity<TimeSlot> getTimeSlotById(@PathVariable long timeSlotId) {
        TimeSlot timeSlot = timeSlotService.getTimeSlotById(timeSlotId);
        return ResponseEntity.ok().body(timeSlot);
    }

//    @GetMapping("/{id}/{date}")
//    public ResponseEntity getAvailableTimeSlotCountForVenueOnDate(@PathVariable Long id, @RequestParam("date") LocalDate date) {
//        return ResponseEntity.ok(timeSlotService.getAvailableTimeSlotCountForVenueOnDate(id, date));
//    }

    // Tạo mới một TimeSlot
    @PostMapping
    public ResponseEntity<TimeSlot> createTimeSlot(@RequestBody TimeSlotRequest timeSlotRequest) {
        TimeSlot timeSlot = timeSlotService.createTimeSlot(timeSlotRequest);
        return new ResponseEntity<>(timeSlot, HttpStatus.CREATED);
    }

    // Cập nhật thông tin một TimeSlot
    @PutMapping("/{timeSlotId}")
    public ResponseEntity<TimeSlot> updateTimeSlot(@PathVariable long timeSlotId, @RequestBody TimeSlotRequest timeSlotDetails) {
        TimeSlot timeSlot = timeSlotService.updateTimeSlot(timeSlotId, timeSlotDetails);
        return ResponseEntity.ok().body(timeSlot);
    }

    // Lấy danh sách TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    @GetMapping("/start/{start}/end/{end}")
    public ResponseEntity<List<TimeSlot>> getTimeSlotsByStartTimeBetween(@PathVariable String start, @PathVariable String end) {
        return ResponseEntity.ok(timeSlotService.findByStartTimeBetween(start, end));
    }


    @GetMapping("/available-slots")
    public ResponseEntity getAvailableTimeSlot(@RequestParam(required = false) String date,
                                               @RequestParam(required = false) Long venueId){//, @PathVariable BookingType bookingType){
        return ResponseEntity.ok(timeSlotService.getAvailableSlots(date,venueId));
    }

    @GetMapping("/venue-slots")
    public ResponseEntity getVenueTimeSlot(@RequestParam(required = false) Long venueId){//, @PathVariable BookingType bookingType){
        return ResponseEntity.ok(timeSlotService.getAllSlotByVenue(venueId));
    }

    @GetMapping("/without-pricing")
    public ResponseEntity<List<TimeSlot>> getTimeSlotsWithoutPricing() {
        List<TimeSlot> timeSlots = timeSlotService.getTimeSlotsWithoutPricing();
        return ResponseEntity.ok(timeSlots);
    }

//    @GetMapping("/available-fixed-slots")
//    public ResponseEntity<List<TimeSlotResponse>> getAvailableSlotByDayOfWeek(
//            @RequestParam(value = "applicationStartDate", required = false) String applicationStartDate,
//            @RequestParam(value = "durationInMonths", required = false) Integer durationInMonths,
//            @RequestParam(value = "dayOfWeek", required = false) List<String> dayOfWeek,
//            @RequestParam(value = "court", required = false) Long court) {
//
//        List<TimeSlotResponse> availableSlots = timeSlotService.getAvailableSlotByDayOfWeek(applicationStartDate, durationInMonths, dayOfWeek, court);
//        return ResponseEntity.ok(availableSlots);
//    }

}

