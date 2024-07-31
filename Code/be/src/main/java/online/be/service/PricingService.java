package online.be.service;

import online.be.entity.Account;
import online.be.entity.Pricing;
import online.be.entity.TimeSlot;
import online.be.entity.Venue;
import online.be.exception.BadRequestException;
import online.be.model.Request.PricingRequest;
import online.be.model.Response.VenueScheduleResponse;
import online.be.repository.PricingRepository;
import online.be.repository.TimeSlotRepository;
import online.be.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PricingService {

    @Autowired
    PricingRepository pricingRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    AuthenticationService authenticationService;

    //set bang gia theo loai booking type, venue va timeslot
    public Pricing createPricingTable(long timeSlotId, PricingRequest request){
        Account manager = authenticationService.getCurrentAccount();
        Venue venue = venueRepository.findVenueByManagerId(manager.getId());
        if(venue == null){
            throw new BadRequestException("Venue not found");
        }
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(()-> new BadRequestException("Timeslot not found"));
        if(!timeSlot.getVenue().equals(venue)){
            throw new BadRequestException("Timeslot does not belong to given venue");
        }
        Pricing pricing = new Pricing();
        pricing.setVenue(venue);
        pricing.setTimeSlot(timeSlot);
        pricing.setPricePerHour(request.getPrice());
        pricing.setBookingType(request.getBookingType());
        return pricingRepository.save(pricing);
    }

    //update bang gia
    public Pricing updatePricingTable(long pricingId, PricingRequest request){
        Pricing pricing = pricingRepository.findById(pricingId)
                .orElseThrow(()-> new BadRequestException("Pricing Table not fond"));
        pricing.setBookingType(request.getBookingType());
        pricing.setPricePerHour(request.getPrice());
        return pricingRepository.save(pricing);
    }

    //lay tat ca bang gia
    public List<Pricing> getAllPricing(){
        return pricingRepository.findAll();
    }

    //lay theo id
    public Pricing getPricigById(long pricingId){
        Pricing pricing = pricingRepository.findById(pricingId).orElseThrow(()
                -> new BadRequestException("Pricing not found with Id: " + pricingId));
        if(pricing == null){
            throw new BadRequestException("Pricing not found");
        }
        return pricing;
    }

    public List<Pricing> getPricingByVenueId(long venueId){
        return pricingRepository.findByVenueId(venueId);
    }

    public VenueScheduleResponse getVenuePricingAndSchedule(long venueId) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new BadRequestException("Venue not found"));
        List<TimeSlot> timeSlots = timeSlotRepository.findByVenueId(venueId);
        List<Pricing> pricings = pricingRepository.findByVenueId(venueId);

        VenueScheduleResponse dto = new VenueScheduleResponse();
        dto.setVenue(venue);
        dto.setTimeSlots(timeSlots);
        dto.setPricingList(pricings);
        return dto;
    }


}
