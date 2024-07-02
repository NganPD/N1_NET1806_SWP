//package online.be.api;
//
//import online.be.entity.TimeSlotPrice;
//import online.be.model.Response.UpdateScheduleResponse;
//import online.be.service.TimeSlotPriceService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/courtschedules")
//public class ScheduleAPI {
//
//    @Autowired
//    TimeSlotPriceService scheduleService;
//
//    // Tạo mới một CourtSchedule
//    @PostMapping("create")
//    public ResponseEntity createCourtSchedule(@RequestBody CreateScheduleRequest courtScheduleRequest) {
//        TimeSlotPrice schedule = scheduleService.createSchedule(courtScheduleRequest);
//        return ResponseEntity.ok(schedule);
//    }
//
//    // Get a list schedule by Time slot
//    @GetMapping("timeslot-id")
//    public ResponseEntity getSchedulesByTimeSlot(@PathVariable long id){
//        List<TimeSlotPrice> list = scheduleService.getSchedulesByTimeSlot(id);
//        return ResponseEntity.ok(list);
//    }
//
//    // Get a list schedule by Court
//    @GetMapping("court-id")
//    public ResponseEntity getSchedulesByCourt(@PathVariable long id){
//        List<TimeSlotPrice> list = scheduleService.getSchedulesByCourt(id);
//        return ResponseEntity.ok(list);
//    }
//
//    @PostMapping("update-available")
//    public  ResponseEntity updateScheduleAvailable(@PathVariable long id, @RequestBody UpdateScheduleRequest request){
//        UpdateScheduleResponse response = scheduleService.updateScheduleAvailable(id,request);
//        return ResponseEntity.ok(response);
//    }
//}
