package online.be.service;

import online.be.entity.Pricing;
import online.be.entity.TimeSlot;
import online.be.entity.Venue;
import online.be.exception.BadRequestException;
import online.be.model.Request.PricingRequest;
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

    //set bang gia theo loai booking type, venue va timeslot
    public Pricing createPricingTable(PricingRequest request){
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(()-> new BadRequestException("Venue not found"));
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
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
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(()-> new BadRequestException("Venue not found"));
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(()-> new BadRequestException("Timeslot not found"));
        if(!timeSlot.getVenue().equals(venue)){
            throw new BadRequestException("Timeslot does not belong to given venue");
        }
        pricing.setBookingType(request.getBookingType());
        pricing.setPricePerHour(request.getPrice());
        pricing.setVenue(venue);
        pricing.setTimeSlot(timeSlot);
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



}
