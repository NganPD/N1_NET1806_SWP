package online.be.service;

import online.be.entity.Court;
import online.be.exception.BadRequestException;
import online.be.model.CreateCourtRequest;
import online.be.model.UpdateCourtRequest;
import online.be.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourtService {

    @Autowired
    private final CourtRepository courtRepository;

    //contructor
    public CourtService(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    //tạo court
    public Court createCourt(CreateCourtRequest createdRequest){

        Court court = new Court();
        court.setCourtName(createdRequest.getName());
        court.setStatus(createdRequest.getStatus());
        court.setAmenities(createdRequest.getAmenities());
        return courtRepository.save(court);
    }

    //lấy court dựa trên id
    public Court getCourtById(long courtId) {
        Optional<Court> courtOptional = courtRepository.findById(courtId);
        if (courtOptional.isPresent()) {
            return courtOptional.get();
        } else {
            throw new RuntimeException("Court not found with ID: " + courtId);
        }
    }

    //show toàn bộ court
    public List<Court> getAllCourts(){
        return courtRepository.findAll();
    }
    //update Court
    public Court updateCourt(Long courtId, UpdateCourtRequest updatedRequest){
        Court court = getCourtById(courtId);
        if(court != null){
            court.setCourtName(updatedRequest.getName());
            court.setStatus(updatedRequest.getStatus());
            court.setAmenities(updatedRequest.getAmenities());

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
