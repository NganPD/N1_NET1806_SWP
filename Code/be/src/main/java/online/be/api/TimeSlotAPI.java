package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.TimeSlot;
import online.be.enums.BookingType;
import online.be.model.Request.TimeSlotRequest;
import online.be.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    // Xóa một TimeSlot
    @DeleteMapping("/{timeSlotId}")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable long timeSlotId) {
        timeSlotService.deleteTimeSlot(timeSlotId);
        return ResponseEntity.noContent().build();
    }

    // Lấy danh sách TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    @GetMapping("/start/{start}/end/{end}")
    public ResponseEntity<List<TimeSlot>> getTimeSlotsByStartTimeBetween(@PathVariable LocalTime start, @PathVariable LocalTime end) {
        return ResponseEntity.ok(timeSlotService.findByStartTimeBetween(start, end));
    }

   @GetMapping("/available-slots")
    public ResponseEntity getAvailableTimeSlot(@RequestParam long courtId, @RequestParam LocalDate date){//, @PathVariable BookingType bookingType){
        return ResponseEntity.ok(timeSlotService.getAvailableSlots(courtId,date));
    }

    @GetMapping("/available-fixed-slots")
    public ResponseEntity<List<TimeSlotResponse>> getAvailableSlotByDayOfWeek(
            @RequestParam String dayOfWeek,
            @RequestParam String applicationDate,
            @RequestParam int durationMonth,
            @RequestParam long courtId) {
        List<TimeSlotResponse> availableSlots = timeSlotService.getAvailableSlotByDayOfWeek(dayOfWeek, applicationDate, durationMonth, courtId);
        return ResponseEntity.ok(availableSlots);
    }
}
