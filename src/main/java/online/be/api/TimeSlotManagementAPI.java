package online.be.api;

import online.be.entity.TimeSlot;
import online.be.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/timeslots")
public class TimeSlotManagementAPI {

    @Autowired
    private TimeSlotService timeSlotService;

    // Lấy tất cả các TimeSlot
    @GetMapping
    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotService.findAll();
    }

    // Lấy TimeSlot theo ID
    @GetMapping("/{id}")
    public ResponseEntity<TimeSlot> getTimeSlotById(@PathVariable Long id) {
        Optional<TimeSlot> timeSlot = timeSlotService.findById(id);
        return timeSlot.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Tạo mới một TimeSlot
    @PostMapping
    public TimeSlot createTimeSlot(@RequestBody TimeSlot timeSlot) {
        return timeSlotService.save(timeSlot);
    }

    // Cập nhật thông tin một TimeSlot
    @PutMapping("/{id}")
    public ResponseEntity<TimeSlot> updateTimeSlot(@PathVariable Long id, @RequestBody TimeSlot timeSlotDetails) {
        Optional<TimeSlot> optionalTimeSlot = timeSlotService.findById(id);
        if (optionalTimeSlot.isPresent()) {
            TimeSlot timeSlot = optionalTimeSlot.get();
            timeSlot.setDuration(timeSlotDetails.getDuration());
            timeSlot.setStartTime(timeSlotDetails.getStartTime());
            timeSlot.setEndTime(timeSlotDetails.getEndTime());
            return ResponseEntity.ok(timeSlotService.save(timeSlot));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa một TimeSlot
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable Long id) {
        if (timeSlotService.findById(id).isPresent()) {
            timeSlotService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy danh sách TimeSlot theo độ dài
    @GetMapping("/duration/{duration}")
    public List<TimeSlot> getTimeSlotsByDuration(@PathVariable int duration) {
        return timeSlotService.findByDuration(duration);
    }

    // Lấy danh sách TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    @GetMapping("/start/{start}/end/{end}")
    public List<TimeSlot> getTimeSlotsByStartTimeBetween(@PathVariable LocalTime start, @PathVariable LocalTime end) {
        return timeSlotService.findByStartTimeBetween(start, end);
    }
}
