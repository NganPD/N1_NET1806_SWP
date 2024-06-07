package online.be.api;

import online.be.entity.CourtSchedule;
import online.be.service.CourtScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courtschedules")
public class CourtScheduleManagementAPI {

    @Autowired
    private CourtScheduleService courtScheduleService;

    // Lấy tất cả các CourtSchedule
    @GetMapping
    public ResponseEntity<List<CourtSchedule>> getAllCourtSchedules() {
        return ResponseEntity.ok(courtScheduleService.findAll());
    }

    // Lấy CourtSchedule theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CourtSchedule> getCourtScheduleById(@PathVariable Long id) {
        Optional<CourtSchedule> courtSchedule = courtScheduleService.findById(id);
        return courtSchedule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Tạo mới một CourtSchedule
    @PostMapping
    public ResponseEntity<CourtSchedule> createCourtSchedule(@RequestBody CourtSchedule courtSchedule) {
        return ResponseEntity.ok(courtScheduleService.save(courtSchedule));
    }

    // Cập nhật thông tin một CourtSchedule
    @PutMapping("/{id}")
    public ResponseEntity<CourtSchedule> updateCourtSchedule(@PathVariable Long id, @RequestBody CourtSchedule courtScheduleDetails) {
        Optional<CourtSchedule> optionalCourtSchedule = courtScheduleService.findById(id);
        if (optionalCourtSchedule.isPresent()) {
            CourtSchedule courtSchedule = optionalCourtSchedule.get();
            courtSchedule.setStatus(courtScheduleDetails.getStatus());
            courtSchedule.setDate(courtScheduleDetails.getDate());
            return ResponseEntity.ok(courtScheduleService.save(courtSchedule));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa một CourtSchedule
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourtSchedule(@PathVariable Long id) {
        if (courtScheduleService.findById(id).isPresent()) {
            courtScheduleService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy danh sách CourtSchedule theo trạng thái
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CourtSchedule>> getCourtSchedulesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(courtScheduleService.findByStatus(status));
    }

    // Lấy danh sách CourtSchedule theo ngày
    @GetMapping("/date/{date}")
    public ResponseEntity<List<CourtSchedule>> getCourtSchedulesByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(courtScheduleService.findByDate(date));
    }
}
