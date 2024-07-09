package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.TimeSlotPrice;
import online.be.model.Request.TimeSlotPriceRequest;
import online.be.model.Response.TimeSlotPriceResponse;
import online.be.repository.TimeSlotPriceRepository;
import online.be.service.TimeSlotPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/time-slot-price")
@SecurityRequirement(name = "api")
public class TimeSlotPriceAPI {

    @Autowired
    TimeSlotPriceService timeSlotPriceService;

    // Lấy tất cả các CourtSchedule
    @GetMapping
    public ResponseEntity<List<TimeSlotPrice>> getAllPrice() {
        return ResponseEntity.ok(timeSlotPriceService.getAllSlotPrice());
    }

    // Tạo mới một CourtSchedule
    @PostMapping
    public ResponseEntity<TimeSlotPriceResponse> createTimeSlotPrice(@RequestBody TimeSlotPriceRequest request) {
        TimeSlotPriceResponse response = timeSlotPriceService.createTimeSlot(request);
        return ResponseEntity.ok(response);
    }

    // Cập nhật thông tin một CourtSchedule
//    @PutMapping("/{id}")
//    public ResponseEntity<TimeSlotPriceResponse> updatePrice(@PathVariable long timeSlotId, @PathVariable long timeSlotPriceId,
//                                                             @RequestBody TimeSlotPrice timeSlotPriceDetaiil) {
//        TimeSlotPriceResponse response = timeSlotPriceService.updateTimeSlotPrice(timeSlotId, timeSlotPriceId, timeSlotPriceDetaiil);
//        return ResponseEntity.ok(response);
//    }

//    // Xóa một CourtSchedule
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCourtSchedule(@PathVariable long id) {
//        courtScheduleService.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // Lấy danh sách CourtSchedule theo trạng thái
//    @GetMapping("/status/{status}")
//    public ResponseEntity<List<CourtSchedule>> getCourtSchedulesByStatus(@PathVariable String status) {
//        return ResponseEntity.ok(courtScheduleService.findByStatus(status));
//    }
//
//    // Lấy danh sách CourtSchedule theo ngày
//    @GetMapping("/date/{date}")
//    public ResponseEntity<List<CourtSchedule>> getCourtSchedulesByDate(@PathVariable LocalDate date) {
//        return ResponseEntity.ok(courtScheduleService.findByDate(date));
//    }
}
