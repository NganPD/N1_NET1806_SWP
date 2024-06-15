package online.be.api;

import online.be.entity.CourtSchedule;
import online.be.model.Request.CourtScheduleRequest;
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
        return ResponseEntity.ok(courtScheduleService.getAllCourtSchedules());
    }

    // Lấy CourtSchedule theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CourtSchedule> getCourtScheduleById(@PathVariable Long id) {
        CourtSchedule courtSchedule = courtScheduleService.getCourtScheduleById(id);
        return ResponseEntity.ok(courtSchedule);
    }

    // Tạo mới một CourtSchedule
    @PostMapping
    public ResponseEntity<CourtSchedule> createCourtSchedule(@RequestBody CourtScheduleRequest courtScheduleRequest) {
        CourtSchedule courtSchedule = courtScheduleService.createSchedule(courtScheduleRequest);
        return ResponseEntity.ok(courtSchedule);
    }

    // Cập nhật thông tin một CourtSchedule
    @PutMapping("/{id}")
    public ResponseEntity<CourtSchedule> updateCourtSchedule(@PathVariable Long id, @RequestBody CourtScheduleRequest courtScheduleDetail) {
        CourtSchedule courtSchedule = courtScheduleService.updateCourtSchedule(id, courtScheduleDetail);
        return ResponseEntity.ok(courtSchedule);
    }

    // Xóa một CourtSchedule
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourtSchedule(@PathVariable long id) {
        courtScheduleService.deleteById(id);
        return ResponseEntity.noContent().build();
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
