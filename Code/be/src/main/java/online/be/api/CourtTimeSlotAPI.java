package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.CourtTimeSlot;
import online.be.model.Request.CourtTimeSlotRequest;
import online.be.service.CourtTimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/court-time-slot")
@SecurityRequirement(name = "api")
public class CourtTimeSlotAPI {

    @Autowired
    CourtTimeSlotService courtTimeSlotService;

    @PostMapping
    public ResponseEntity createCourtTimeSlot(@RequestBody CourtTimeSlotRequest request){
        CourtTimeSlot courtTimeSlot = courtTimeSlotService.createCourtTimeSlot(request);
        return ResponseEntity.ok(courtTimeSlot);
    }

}
