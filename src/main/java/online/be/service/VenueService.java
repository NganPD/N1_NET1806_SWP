package online.be.service;

import online.be.entity.Court;
import online.be.entity.Venue;
import online.be.repository.VenueRepostiory;

import java.util.List;
import java.util.Optional;

public class VenueService {

    VenueRepostiory venueRepostiory;

    //contructor

    public VenueService(VenueRepostiory venueRepostiory) {
        this.venueRepostiory = venueRepostiory;
    }

    //tạo một venue
    public Venue createVenue (Venue venue){
        return venueRepostiory.save(venue);
    }

    //get venue by id

    public Venue getVenueById(Long venueId){
        Optional<Venue> venueOptional = venueRepostiory.findById(venueId);
        if(venueOptional.isPresent()){
            return venueOptional.get();
        }else{
            throw new RuntimeException(("Venue not found with ID: " + venueId));
        }
    }

    //getAllVenue
    public List<Venue> getAllVenues(){
        return venueRepostiory.findAll();
    }

    //update venue infomration
    public Venue updateVenue(Long venueId, Venue venueDetails){
        Optional<Venue> venueOptional = venueRepostiory.findById(venueId);
        if(venueOptional.isPresent()){
            Venue venue = venueOptional.get();
            venue.setName(venueDetails.getName());
            venue.setAddress(venueDetails.getAddress());
            venue.setDescription(venueDetails.getDescription());
            venue.setOperatingHours(venueDetails.getOperatingHours());
            venue.setPaymentInfor(venue.getPaymentInfor());
            venue.setNumberOfCourts(venueDetails.getNumberOfCourts());
            venue.setRating(venueDetails.getRating());
            venue.setImageURL(venueDetails.getImageURL());
            return venueRepostiory.save(venue);
        }else{
            throw new RuntimeException("Venue not found with ID: " + venueId);
        }
    }

    //delete venue
    public void deleteVenue(Long venueId){
        venueRepostiory.deleteById(venueId);
    }
}
