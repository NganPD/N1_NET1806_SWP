package online.be.service;

import online.be.entity.Court;
import online.be.entity.Venue;
import online.be.enums.CourtStatus;
import online.be.exception.BadRequestException;
import online.be.model.Request.CreateCourtRequest;
import online.be.model.Request.UpdateCourtRequest;
import online.be.repository.CourtRepository;
import online.be.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class CourtService {

    @Autowired
    CourtRepository courtRepository;
    @Autowired
    VenueRepository venueRepository;


    //tạo court
    public Court createCourt(CreateCourtRequest courtRequest) {
        //kiem tra venue co ton tai hay khong
        Venue venue = venueRepository.findById(courtRequest.getVenueId())
                .orElseThrow(() -> new BadRequestException("Venue not found with Id " + courtRequest.getVenueId()));
        Court court = new Court();
        court.setCourtName(courtRequest.getCourtName());
        court.setCourtType(courtRequest.getCourtType());
        court.setStatus(courtRequest.getStatus());
        court.setDescription(courtRequest.getDescription());
        court.setAmenities(courtRequest.getAmenities());
        court.setVenue(venue);

        try {
            return courtRepository.save(court);
        }catch (DataIntegrityViolationException e){
            throw new RuntimeException(e);
        }
    }
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    //lấy court dựa trên id
    public Court getCourtById(long courtId) {
        Court court = courtRepository.findById(courtId).get();
        if (court == null) {
            throw new RuntimeException("CourtId is not existed: " + courtId);
        }
        return court;
    }

    //show toàn bộ court
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    //update Court
    public Court updateCourt(UpdateCourtRequest courtRequest) {
        //kiem tra court co ton tai hay khong
        Court court = courtRepository.findById(courtRequest.getCourtId())
                .orElseThrow(() -> new BadRequestException("Court not found with Id: "+ courtRequest.getCourtId()));
        //kiem tra ten court
        if(!Pattern.matches("^[a-zA-Z\\s]+$", courtRequest.getCourtName())){
            throw new BadRequestException("Court name contains invalid characters.");
        }
        //kiem tra venue co ton tai hay khong
        Venue venue = venueRepository.findById(courtRequest.getVenueId())
                .orElseThrow(()-> new BadRequestException("Venue not found with Id: " + courtRequest.getVenueId()));

        court.setCourtName(courtRequest.getCourtName());
        court.setCourtType(courtRequest.getCourtType());
        court.setStatus(courtRequest.getStatus());
        court.setDescription(courtRequest.getDescription());
        court.setVenue(venue);

        try{
            return courtRepository.save(court);
        }catch (DataIntegrityViolationException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    //delete Court
    public void deleteCourt(Long courtId) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(()->new RuntimeException("Court not found with Id: " + courtId));
        court.setStatus(CourtStatus.INACTIVE);
    }

//    //get the list of court base on their type
//    public List<Court> findCourtByType(String courtType){
//        return courtRepository.findCourtByType(courtType);
//    }



    
}


