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
    public ResponseEntity<Venue> updateVenue(@PathVariable long venueId,@RequestBody Venue venueDetails){
        Venue updatedVenue = venueService.updateVenue(venueId, venueDetails);
        return ResponseEntity.ok().body(updatedVenue);
    }

    @DeleteMapping("/{venueId}")
    public ResponseEntity<Venue> deleteVenue(@PathVariable long venueId){
        venueService.deleteVenue(venueId);
        return ResponseEntity.noContent().build();//204
    }

}
