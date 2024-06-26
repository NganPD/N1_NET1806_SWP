package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.TimeSlot;
import online.be.model.Request.TimeSlotRequest;
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
    @GetMapping("/{id}")
    public ResponseEntity<TimeSlot> getTimeSlotById(@PathVariable long timeSlotId) {
        TimeSlot timeSlot = timeSlotService.getTimeSlotById(timeSlotId);
        return ResponseEntity.ok().body(timeSlot);
    }

    // Tạo mới một TimeSlot
    @PostMapping
    public ResponseEntity<TimeSlot> createTimeSlot(@RequestBody TimeSlotRequest timeSlotRequest) {
        TimeSlot timeSlot = timeSlotService.createTimeSlot(timeSlotRequest);
        return new ResponseEntity<>(timeSlot, HttpStatus.CREATED);
    }

    // Cập nhật thông tin một TimeSlot
    @PutMapping("/{id}")
    public ResponseEntity<TimeSlot> updateTimeSlot(@PathVariable long timeSlotId, @RequestBody TimeSlotRequest timeSlotDetails) {
        TimeSlot timeSlot = timeSlotService.updateTimeSlot(timeSlotId, timeSlotDetails);
        return ResponseEntity.ok().body(timeSlot);
    }

    // Xóa một TimeSlot
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable long timeSlotId) {
        timeSlotService.deleteTimeSlot(timeSlotId);
        return ResponseEntity.noContent().build();
    }

    // Lấy danh sách TimeSlot theo độ dài
    @GetMapping("/duration/{duration}")
    public ResponseEntity<List<TimeSlot>> getTimeSlotsByDuration(@PathVariable int duration) {
        return ResponseEntity.ok(timeSlotService.findByDuration(duration));
    }

    // Lấy danh sách TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    @GetMapping("/start/{start}/end/{end}")
    public ResponseEntity<List<TimeSlot>> getTimeSlotsByStartTimeBetween(@PathVariable LocalTime start, @PathVariable LocalTime end) {
        return ResponseEntity.ok(timeSlotService.findByStartTimeBetween(start, end));
    }

    @GetMapping("/venue/{venueId}/court/{courtId}/excludeDate")
    public ResponseEntity<List<TimeSlot>> getTimeSlotsByVenueAndCourtExcludingDate(
            @PathVariable Long venueId,
            @PathVariable Long courtId,
            @RequestParam("date") LocalDate date) {

        List<TimeSlot> timeSlots = timeSlotService.getTimeSlotsByVenueAndCourtExcludingDate(venueId, courtId, date);

        return ResponseEntity.ok().body(timeSlots);
    }
}