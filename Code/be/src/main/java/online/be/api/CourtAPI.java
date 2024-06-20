package online.be.api;

import online.be.entity.Court;
import online.be.model.Request.CourtRequest;
import online.be.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
<<<<<<< HEAD
@RequestMapping("api/courts")
@SecurityRequirement(name = "api")
public class CourtAPI {

    @Autowired
    CourtService courtService;

=======
@RequestMapping("/api/courts")
public class CourtAPI {
    @Autowired
    private CourtService courtService;
>>>>>>> origin/feat/AccountManagerAPI

    @PostMapping
    public ResponseEntity<Court> createCourt(@RequestBody CourtRequest courtRequest){
        Court createdCourt = courtService.createCourt(courtRequest);
        return new ResponseEntity<>(createdCourt, HttpStatus.CREATED);
    }

    @GetMapping("/{courtId}")
    public ResponseEntity<Court> getCourtById(@PathVariable long courtId){
        Court court = courtService.getCourtById(courtId);
        return ResponseEntity.ok().body(court);
    }

    @PutMapping("/{courtId}")
    public ResponseEntity<Court> updateCourt(@PathVariable long courtId, @RequestBody CourtRequest courtDetails){
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
