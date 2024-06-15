package online.be.service;

import online.be.entity.Court;
import online.be.entity.Venue;
import online.be.model.Request.CourtRequest;
import online.be.repository.CourtRepository;
import online.be.repository.VenueRepostiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourtService {

    @Autowired
    CourtRepository courtRepository;
    @Autowired
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
            throw new RuntimeException("VenueId is not existed: " + courtRequest.getVenueId());//Tự handle lỗi để front end nhận được
        }
    }
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    //lấy court dựa trên id
    public Court getCourtById(long courtId){
        Court court = courtRepository.findById(courtId).get();
        if(court == null){
            throw new RuntimeException("CourtId is not existed: " + courtId);//Tự handle lỗi để front end nhận được
        }
        return court;
    }

    //show toàn bộ court
    public List<Court> getAllCourts(){
        return courtRepository.findAll();
    }//Tự tạo hiển thị không có schedule

    //update Court
    public Court updateCourt(long courtId, CourtRequest courtRequest){
        //kiểm tra xem court có tồn tại hay không
        Court court = getCourtById(courtId);
        if(court == null){
            throw new RuntimeException("CourtId is not existed: " + courtId);
        }
        //kiểm tra xem venue tồn tại hay không
        Venue venue = venueRepostiory.findById(courtRequest.getVenueId()).get();
        if(venue == null) {
            throw new RuntimeException("VenueId is not existed: " + courtRequest.getVenueId());//Tự handle lỗi để front end nhận được
        }

        //kiểm tra rằng court này có tồn tại bên trong venue này không

        if(!venue.getCourts().contains(courtId)){
            throw new RuntimeException("Court with ID: " + courtId + "does not belong to Venue with ID: " + courtRequest.getVenueId());//Tự handle lỗi để front end nhận được
        }

        //update lại các thông tin bên trong court
        court.setCourtName(courtRequest.getCourtName());
        court.setStatus(courtRequest.isStatus());
        court.setAmenities(courtRequest.getAmenities());
        court.setVenue(venue);

        return courtRepository.save(court);
    }
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    //delete Court
    public void deleteCourt(Long courtId){
    courtRepository.deleteById(courtId);
}//Tự tạo hiển thị không có court

}
