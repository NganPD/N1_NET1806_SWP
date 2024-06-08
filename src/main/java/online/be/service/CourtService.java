package online.be.service;

import online.be.entity.Court;
import online.be.entity.Venue;
import online.be.model.Request.CourtRequest;
import online.be.repository.CourtRepository;
import online.be.repository.VenueRepostiory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourtService {

    CourtRepository courtRepository;
    VenueRepostiory venueRepostiory;

    //contructor
    public CourtService(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    //tạo court
    public Court createCourt(CourtRequest courtRequest){
        Court court = new Court();
        Venue venue = venueRepostiory.findById(courtRequest.getVenueId()).get();
        if(venue != null){
            court.setCourtName(courtRequest.getCourtName());
            court.setStatus(courtRequest.isStatus());
            court.setAmenities(courtRequest.getAmenities());
            court.setVenue(venue);
            return courtRepository.save(court);
        } else {
            throw new RuntimeException("VenueId is not existed: " + courtRequest.getVenueId());
        }
    }

    //lấy court dựa trên id
    public Court getCourtById(long courtId){
        Optional<Court> courtOptional = courtRepository.findById(courtId);
        if(courtOptional.isPresent()){
            return courtOptional.get();
        }else{
            throw new RuntimeException("Court not found with ID: " + courtId);
        }
    }

    //show toàn bộ court
    public List<Court> getAllCourts(){
        return courtRepository.findAll();
    }
    //update Court
    public Court updateCourt(long courtId, CourtRequest courtDetails){
        Optional<Court> courtOptional = courtRepository.findById(courtId);
        if(courtOptional.isPresent()){
            Court court = new Court();
            Venue venue = venueRepostiory.findById(courtDetails.getVenueId()).get();
            if(venue != null){
                court.setCourtName(courtDetails.getCourtName());
                court.setStatus(courtDetails.isStatus());
                court.setAmenities(courtDetails.getAmenities());
                court.setVenue(venue);
                return courtRepository.save(court);
            } else {
                throw new RuntimeException("VenueId is not existed: " + courtDetails.getVenueId());
            }
        }else{
            throw new RuntimeException("Court not found with ID: " + courtId);
        }
    }

    //delete Court
    public void deleteCourt(Long courtId){
        courtRepository.deleteById(courtId);
    }

}
