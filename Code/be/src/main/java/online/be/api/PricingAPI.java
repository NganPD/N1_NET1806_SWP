package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.model.Request.PricingRequest;
import online.be.model.Response.VenueScheduleResponse;
import online.be.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.OverlappingFileLockException;

@RestController
@RequestMapping("/api/pricing")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class PricingAPI {
    @Autowired
    PricingService pricingService;

    @PostMapping("timeSlots/{timeSlotId}/create")
    public ResponseEntity createPricing(@PathVariable long timeSlotId, @RequestBody PricingRequest request){
        return ResponseEntity.ok(pricingService.createPricingTable(timeSlotId,request));
    }

    @PutMapping("/{pricingId}/update")
    public ResponseEntity updatePricing(@PathVariable long pricingId, @RequestBody PricingRequest request){
        return ResponseEntity.ok(pricingService.updatePricingTable(pricingId, request));
    }

    @GetMapping
    public ResponseEntity getAllPricing(){
        return ResponseEntity.ok(pricingService.getAllPricing());
    }

    @GetMapping("/{pricingId}")
    public ResponseEntity getPricingById(@PathVariable long pricingId){
        return ResponseEntity.ok(pricingService.getPricigById(pricingId));
    }

    @GetMapping("/{venueId}/pricing-table-of-venue")
    public ResponseEntity getPricingByVenueId(@PathVariable long venueId){
        return ResponseEntity.ok(pricingService.getPricingByVenueId(venueId));
    }

    @GetMapping("/{venueId}/pricing-and-schedule")
    public ResponseEntity getVenuePricingAndSchedule(@PathVariable long venueId) {
        VenueScheduleResponse dto = pricingService.getVenuePricingAndSchedule(venueId);
        return ResponseEntity.ok(dto);
    }
}
