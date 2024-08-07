package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import online.be.entity.Venue;
import online.be.model.Request.CreateVenueRequest;
import online.be.model.Request.UpdateVenueRequest;
import online.be.model.Response.VenueResponse;
import online.be.service.VenueService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/venues")
@SecurityRequirement(name = "api")
public class VenueAPI {

    @Autowired
    VenueService venueService;

    @PostMapping
    public ResponseEntity createVenue(@RequestBody CreateVenueRequest request) {
        Venue createdVenue = venueService.createVenue(request);
        return ResponseEntity.ok().body(createdVenue);
    }

    @GetMapping("/{venueId}")
    public ResponseEntity getVenueById(@PathVariable long venueId) {
        Venue venue = venueService.getVenueById(venueId);
        return ResponseEntity.ok().body(venue);
    }

    @GetMapping
    public ResponseEntity getAllVenue() {
        return ResponseEntity.ok().body(venueService.getAllVenues());
    }

    @PutMapping("/update")
    public ResponseEntity updateVenue(@RequestBody UpdateVenueRequest updateVenueRequest) {
        Venue updatedVenue = venueService.updateVenue(updateVenueRequest);
        return ResponseEntity.ok().body(updatedVenue);
    }

    //lấy theo tên
    @GetMapping("/search-keyword")
    public ResponseEntity searchVenuesByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(venueService.searchVenuesByKeyword(keyword));
    }


    @DeleteMapping("/{venueId}")
    public ResponseEntity deleteVenue(@PathVariable long venueId) {
        venueService.deleteVenue(venueId);
        return ResponseEntity.noContent().build();//204
    }

    @GetMapping("/manager")
    public ResponseEntity getManager() {
        return ResponseEntity.ok(venueService.getManager());
    }

    @GetMapping("/search")
    public ResponseEntity<List<VenueResponse>> searchVenues(
            @RequestParam(required = false) String operatingHoursStr,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String timeStr,
            @RequestParam(required = false) String keyWord) {
        List<VenueResponse> venueResponses = venueService.searchVenues(operatingHoursStr, location, timeStr, keyWord);
        return ResponseEntity.ok(venueResponses);
    }

    @PostMapping("/{venueId}/staff/{staffId}")
    public ResponseEntity addStaffToVenue(@PathVariable long venueId, @PathVariable long staffId) {
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
    public ResponseEntity getCourtsByVenueId(@PathVariable long venueId) {
        return ResponseEntity.ok(venueService.getCourtByVenueId(venueId));
    }

    @GetMapping("{venueId}/reviews")
    public ResponseEntity getReviewsByVenueId(@PathVariable long venueId) {
        return ResponseEntity.ok(venueService.getReviewByVenueId(venueId));
    }

    @GetMapping("/number-venues")
    public ResponseEntity getNumberOfVenues() {
        return ResponseEntity.ok(venueService.numberOfVenues());
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<Venue> getVenueByManagerId(@PathVariable Long managerId) {
        Venue venue = venueService.getVenueByManagerId(managerId);
        return ResponseEntity.ok(venue);
    }



}
