package online.be.api;


import online.be.entity.Court;
import online.be.model.CreateCourtRequest;
import online.be.model.UpdateCourtRequest;
import online.be.service.CourtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
public class CourtManagementAPI {

    CourtService courtService;

    public CourtManagementAPI(CourtService courtService) {
        this.courtService = courtService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Court> createCourt(@RequestBody CreateCourtRequest createCourtRequest){
        Court createdCourt = courtService.createCourt(createCourtRequest);
        return new ResponseEntity<>(createdCourt, HttpStatus.CREATED);
    }

    @GetMapping("/{courtId}")
    public ResponseEntity<Court> getCourtById(@PathVariable Long courtId){
        Court court = courtService.getCourtById(courtId);
        return ResponseEntity.ok().body(court);
    }

    @PutMapping("/{courtId}")
    public ResponseEntity<Court> updateCourt(@PathVariable Long courtId, @RequestBody UpdateCourtRequest updateCourtRequest){
        Court updatedCourt = courtService.updateCourt(courtId, updateCourtRequest);
        return ResponseEntity.ok().body(updatedCourt);
    }

    @DeleteMapping("/{courtId}")
    public ResponseEntity<Court> deleteCourt(@PathVariable Long courtId){
        courtService.deleteCourt(courtId);
        return ResponseEntity.noContent().build();//trả về 204 No Content
    }

    @GetMapping
    public ResponseEntity<List<Court>> getAllCourts(){
        List<Court> courts = courtService.getAllCourts();
        return ResponseEntity.ok().body(courts);
    }
}
