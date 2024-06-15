package online.be.service;

import online.be.entity.Court;
import online.be.entity.Venue;
import online.be.enums.CourtStatus;
import online.be.model.Request.CreateCourtRequest;
import online.be.model.Request.UpdateCourtRequest;
import online.be.repository.CourtRepository;
import online.be.repository.VenueRepostiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {

    @Autowired
    CourtRepository courtRepository;
    @Autowired
    VenueRepostiory venueRepostiory;


    //tạo court
    public Court createCourt(CreateCourtRequest courtRequest) {
        Court court = new Court();
        court.setCourtName(courtRequest.getCourtName());
        court.setStatus(courtRequest.getStatus());
        court.setAmenities(courtRequest.getAmenities());
        return courtRepository.save(court);
    }

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
    public Court updateCourt(long courtId, UpdateCourtRequest courtRequest) {
        //kiểm tra xem Venue có tồn tại hay không
        Venue venue = venueRepostiory.findById(courtRequest.getVenueId()).get();
        if (venue != null) {
            //kiểm tra xem court tồn tại hay không
            Court court = courtRepository.findById(courtId).get();
            if (court != null) {
                //update lại các thông tin bên trong court
                court.setCourtName(courtRequest.getCourtName());
                court.setStatus(courtRequest.getStatus());
                court.setAmenities(courtRequest.getAmenities());
                court.setVenue(venue);
                return courtRepository.save(court);
            }else{
                throw new RuntimeException("Not found with CourtID: " + courtId);
        }
        }else{
            throw new RuntimeException("Not found VenueId: " + courtRequest.getVenueId());
        }
    }

    //delete Court
    public void deleteCourt(Long courtId) {
        Court court = courtRepository.findById(courtId).get();
        if (court != null) {
            court.setStatus(CourtStatus.INACTIVE);
        } else {
            throw new RuntimeException("CourtId is not existed: " + courtId);
        }
    }
}


