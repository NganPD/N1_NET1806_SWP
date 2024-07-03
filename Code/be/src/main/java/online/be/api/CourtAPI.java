package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Court;
import online.be.model.Request.CreateCourtRequest;
import online.be.model.Request.UpdateCourtRequest;
import online.be.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/courts")
@SecurityRequirement(name = "api")
public class CourtAPI {

    @Autowired
    CourtService courtService;


    @PostMapping
    public ResponseEntity createCourt(@RequestBody CreateCourtRequest courtRequest){
        Court createdCourt = courtService.createCourt(courtRequest);
        return ResponseEntity.ok().body(createdCourt);
    }

    @GetMapping("/{courtId}")
    public ResponseEntity getCourtById(@PathVariable long courtId){
        Court court = courtService.getCourtById(courtId);
        return ResponseEntity.ok().body(court);
    }

    @PutMapping("/{courtId}")
    public ResponseEntity updateCourt(@RequestBody UpdateCourtRequest updateCourtRequest, @PathVariable long courtId){
        Court updatedCourt = courtService.updateCourt(updateCourtRequest, courtId);
        return ResponseEntity.ok().body(updatedCourt);
    }

    @DeleteMapping("/{courtId}")
    public ResponseEntity deleteCourt(@PathVariable long courtId){
        courtService.deleteCourt(courtId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Court>> getAllCourts(){
        List<Court> courts = courtService.getAllCourts();
        return ResponseEntity.ok().body(courts);
    }
}
