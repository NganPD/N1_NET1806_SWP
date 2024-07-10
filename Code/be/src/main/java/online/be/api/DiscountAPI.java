package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Discount;
import online.be.model.Request.DiscountRequest;
import online.be.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/time-slot-price")
@SecurityRequirement(name = "api")
public class DiscountAPI {

    @Autowired
    DiscountService discountService;

    @GetMapping
    public ResponseEntity<List<Discount>> getAllPrice() {
        return ResponseEntity.ok(discountService.getAllSlotPrice());
    }

    // Tạo mới một CourtSchedule
    @PostMapping
    public ResponseEntity createTimeSlotPrice(@RequestBody DiscountRequest request) {
        Discount response = discountService.createDiscountTable(request);
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
