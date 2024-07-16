package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.model.Request.PricingRequest;
import online.be.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricing")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class PricingAPI {
    @Autowired
    PricingService pricingService;

    @PostMapping("/create")
    public ResponseEntity createPricing(@RequestBody PricingRequest request){
        return ResponseEntity.ok(pricingService.createPricingTable(request));
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
}
