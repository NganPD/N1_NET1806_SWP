package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.service.CourtTimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/court-time-slot")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class CourtTimeSlotAPI {

    @Autowired
    CourtTimeSlotService courtTimeSlotService;

    @GetMapping("/booked-court-slot")
    public ResponseEntity getBookedCourtSlot(long venueId){
        return ResponseEntity.ok(courtTimeSlotService.getBookedSlot(venueId));
    }

    @GetMapping("/booked-or-checked")
    public ResponseEntity getBookedAndCheckedSlot(){
        return ResponseEntity.ok(courtTimeSlotService.getBookedAndCheckedByVenue());
    }
}
