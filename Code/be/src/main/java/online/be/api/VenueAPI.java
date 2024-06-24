package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Venue;
import online.be.model.Request.CreateVenueRequest;
import online.be.model.Request.UpdateVenueRequest;
import online.be.service.VenueService;
import org.hibernate.sql.Update;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("api/venue")
@SecurityRequirement(name = "api")
public class VenueAPI {

    VenueService venueService;

    public VenueAPI(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping
    public ResponseEntity<Venue> createVenue(@RequestBody CreateVenueRequest createVenueRequest){
        Venue createdVenue = venueService.createVenue(createVenueRequest);
        return ResponseEntity.ok().body(createdVenue);
    }

    @GetMapping("/{venueId}")
    public ResponseEntity<Venue> getVenueById(@RequestBody long venueId){
        Venue venue = venueService.getVenueById(venueId);
        return ResponseEntity.ok().body(venue);
    }

    @GetMapping
    public ResponseEntity<List<Venue> > getAllVenue(){
        List<Venue> venues = venueService.getAllVenues();
        return ResponseEntity.ok().body(venues);
    }

    @PutMapping("/{venueId}")
    public ResponseEntity<Venue> updateVenue(@PathVariable long venueId, @RequestBody UpdateVenueRequest updateVenueRequest){
        Venue updatedVenue = venueService.updateVenue(venueId, updateVenueRequest);
        return ResponseEntity.ok().body(updatedVenue);
    }

    //lay theo availableslot
    @GetMapping("/search-venues-availableslots")
    public ResponseEntity<List<Venue>> findVenuesWithAvailableSlots(
            @RequestParam String startTime, @RequestParam String endTime
    ){
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        List<Venue> venues = venueService.getVenueWithAvailableSlots(start, end);
        return ResponseEntity.ok(venues);
    }

    //lấy theo tên
//    @GetMapping("/search-venues-name")
//    public ResponseEntity searchVenues(@RequestParam("keyword") String keyword){
//        List<Venue> venues = venueService.searchvenues(keyword);
//        return ResponseEntity.ok(venues);
//    }

    @DeleteMapping("/{venueId}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long venueId){
        venueService.deleteVenue(venueId);
        return ResponseEntity.noContent().build();//204
    }

    @GetMapping("/search")
    public ResponseEntity<List<Venue>> searchVenues(@RequestParam String operatingHours){
        List<Venue> venues = venueService.searchVenuesByOperatingHours(operatingHours);
        return ResponseEntity.ok(venues);
    }




}
