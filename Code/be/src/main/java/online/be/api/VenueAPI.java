package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Account;
import online.be.entity.Venue;
import online.be.model.Request.CreateVenueRequest;
import online.be.model.Request.UpdateVenueRequest;
import online.be.model.Response.VenueResponse;
import online.be.service.VenueService;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("api/venue")
@SecurityRequirement(name = "api")
public class VenueAPI {

    @Autowired
    VenueService venueService;

    @PostMapping
    public ResponseEntity<Venue> createVenue(@RequestBody CreateVenueRequest createVenueRequest){
        Venue createdVenue = venueService.createVenue(createVenueRequest);
        return ResponseEntity.ok().body(createdVenue);
    }

    @GetMapping("/{venueId}")
    public ResponseEntity getVenueById(@PathVariable long venueId){
        Venue venue = venueService.getVenueById(venueId);
        return ResponseEntity.ok().body(venue);
    }

    @GetMapping
    public ResponseEntity getAllVenue(){
        List<Venue> venues = venueService.getAllVenues();
        return ResponseEntity.ok().body(venues);
    }

    @PutMapping("/{venueId}")
    public ResponseEntity updateVenue(@PathVariable long venueId, @RequestBody UpdateVenueRequest updateVenueRequest){
        Venue updatedVenue = venueService.updateVenue(venueId, updateVenueRequest);
        return ResponseEntity.ok().body(updatedVenue);
    }

    //lấy theo tên
    @GetMapping("/search-keyword")
    public ResponseEntity searchVenuesByKeyword(@RequestParam String keyword){
        List<Venue> venues = venueService.searchVenuesByKeyword(keyword);
        return ResponseEntity.ok(venues);
    }
//
//    //get venue by address
//    @GetMapping("/search-address")
//    public ResponseEntity searchVenuesByAddress(@RequestParam String address){
//        List<Venue> venues = venueService.searchVenuesByAddress(address);
//        return ResponseEntity.ok(venues);
//    }

    @DeleteMapping("/{venueId}")
    public ResponseEntity deleteVenue(@PathVariable long venueId){
        venueService.deleteVenue(venueId);
        return ResponseEntity.noContent().build();//204
    }


    @GetMapping("/search")
    public ResponseEntity<List<VenueResponse>> searchVenues(
            @RequestParam(required = false) String operatingHoursStr,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String timeStr) {
        List<VenueResponse> venueResponses = venueService.searchVenues(operatingHoursStr, location, timeStr);
        return ResponseEntity.ok(venueResponses);
    }

    @PostMapping("/{venueId}/staff/{staffId}")
    public ResponseEntity addStaffToVenue(@PathVariable long venueId, @PathVariable long staffId){
        Venue venue = venueService.addStaffToVenue(staffId, venueId);
        return ResponseEntity.ok(venue);
    }
//
//    @GetMapping("{venueId}/manager")
//    public ResponseEntity<Account> getManager(@PathVariable long venueId){
//        return ResponseEntity.ok(venueService.getManager(venueId));
//    }
//
//    @GetMapping("{venueId}/staffs")
//    public ResponseEntity<List<Account>> getStaffs(@PathVariable long venueId){
//        return ResponseEntity.ok(venueService.getStaffsByVenueId(venueId));
//    }

    @GetMapping("{venueId}/courts")
    public ResponseEntity getCourtsByVenueId(@PathVariable long venueId){
        return ResponseEntity.ok(venueService.getCourtByVenueId(venueId));
    }

    @GetMapping("{venueId}/reviews")
    public ResponseEntity getReviewsByVenueId(@PathVariable long venueId){
        return ResponseEntity.ok(venueService.getReviewByVenueId(venueId));
    }




}
