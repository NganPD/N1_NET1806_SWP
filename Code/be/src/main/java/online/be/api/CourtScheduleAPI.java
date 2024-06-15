//package online.be.api;
//
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import online.be.entity.CourtSchedule;
//import online.be.model.Request.CourtScheduleRequest;
//import online.be.service.CourtScheduleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("api/courtSchedule")
//@SecurityRequirement(name = "api")
//public class CourtScheduleAPI {
//
//    @Autowired
//    private CourtScheduleService courtScheduleService;
//
//    // Lấy tất cả các CourtSchedule
//    @GetMapping
//    public ResponseEntity getAllCourtSchedules() {
//        return ResponseEntity.ok(courtScheduleService.findAll());
//    }
//
//    // Lấy CourtSchedule theo ID
//    @GetMapping("/{courtScheduleId}")
//    public ResponseEntity getCourtScheduleById(@PathVariable Long id) {
//        CourtSchedule courtSchedule = courtScheduleService.findById(id);
//        return ResponseEntity.ok().body(courtSchedule);
//    }
//
//    // Tạo mới một CourtSchedule
//    @PostMapping
//    public ResponseEntity createCourtSchedule(@RequestBody CourtScheduleRequest courtScheduleRequest) {
//        CourtSchedule schedule = courtScheduleService.createSchedule(courtScheduleRequest);
//        return ResponseEntity.ok().body(schedule);
//    }
//
//    // Cập nhật thông tin một CourtSchedule
//    @PutMapping("/{courtScheduleId}")
//    public ResponseEntity updateCourtSchedule(@PathVariable Long id, @RequestBody CourtScheduleRequest request) {
//        CourtSchedule schedule = courtScheduleService.updateSchedule(id,request);
//        return ResponseEntity.ok().body(schedule);
//    }
//
//    // Xóa một CourtSchedule
//    @DeleteMapping("/{courtScheduleId}")
//    public ResponseEntity deleteCourtSchedule(@PathVariable long id) {
//        courtScheduleService.deleteSchedule(id);
//        return ResponseEntity.noContent().build();//204
//    }
//
//}
