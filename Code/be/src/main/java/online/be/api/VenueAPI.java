package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Venue;
import online.be.model.Request.VenueRequest;
import online.be.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Venue> createVenue(@RequestBody VenueRequest venueRequest){
        Venue createdVenue = venueService.createVenue(venueRequest);
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
    public ResponseEntity<Venue> updateVenue(@PathVariable long venueId,@RequestBody VenueRequest venueRequest){
        Venue updatedVenue = venueService.updateVenue(venueId, venueRequest);
        return ResponseEntity.ok().body(updatedVenue);
    }

    //lay theo availableslot
//    @GetMapping("/search-venues-availableslots")
//    public ResponseEntity findVenuesWithAvailableSlots(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime){
//        List<Venue> venues = venueService.getVenueWithAvailableSlots(startDateTime,endDateTime);
//        return ResponseEntity.ok(venues);
//    }

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



}
