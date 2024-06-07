package online.be.service;

import online.be.entity.Court;
import online.be.repository.CourtRepository;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourtService {

    CourtRepository courtRepository;

    //contructor
    public CourtService(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    //tạo court
    public Court createCourt(Court court){
        return courtRepository.save(court);
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
    public Court updateCourt(long courtId, Court courtDetails){
        Optional<Court> courtOptional = courtRepository.findById(courtId);
        if(courtOptional.isPresent()){
            Court court = courtOptional.get();
            court.setCourtName(courtDetails.getCourtName());
            court.setDescription(courtDetails.getDescription());
            court.setLocation(courtDetails.getLocation());
            court.setNumberOfCourts(courtDetails.getNumberOfCourts());
            court.setOperatingHours(courtDetails.getOperatingHours());
            court.setVenue(courtDetails.getVenue());
            return courtRepository.save(court);
        }else{
            throw new RuntimeException("Court not found with ID: " + courtId);
        }
    }

    //delete Court
    public void deleteCourt(Long courtId){
        courtRepository.deleteById(courtId);
    }

}
