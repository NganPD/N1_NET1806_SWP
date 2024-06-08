package online.be.api;

import online.be.entity.Court;
import online.be.service.CourtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
public class CourtManagementAPI {

    private final CourtService courtService;

    public CourtManagementAPI(CourtService courtService) {
        this.courtService = courtService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Court> createCourt(@RequestBody Court court){
        Court createdCourt = courtService.createCourt(court);
        return new ResponseEntity<>(createdCourt, HttpStatus.CREATED);
    }

    @GetMapping("/{courtId}")
    public ResponseEntity<Court> getCourtById(@PathVariable long courtId){
        Court court = courtService.getCourtById(courtId);
        return ResponseEntity.ok().body(court);
    }

    @PutMapping("/{courtId}")
    public ResponseEntity<Court> updateCourt(@PathVariable long courtId, @RequestBody Court courtDetails){
        Court updatedCourt = courtService.updateCourt(courtId, courtDetails);
        return ResponseEntity.ok().body(updatedCourt);
    }

    @DeleteMapping("/{courtId}")
    public ResponseEntity<Void> deleteCourt(@PathVariable long courtId){
        courtService.deleteCourt(courtId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Court>> getAllCourts(){
        List<Court> courts = courtService.getAllCourts();
        return ResponseEntity.ok().body(courts);
    }
}
