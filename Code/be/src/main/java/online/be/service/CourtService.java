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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {

    @Autowired
    CourtRepository courtRepository;
    @Autowired
    VenueRepository venueRepository;


    //tạo court
    public Court createCourt(CreateCourtRequest courtRequest) {
        //Find the venue ỏ throw exception if not found
        Venue existingVenue = venueRepository.findById(courtRequest.getVenueId())
                .orElseThrow(()-> new BadRequestException("The venue does not exist"));

        //Create a new Court
        Court court = new Court();
        court.setCourtName(courtRequest.getCourtName());
        court.setStatus(courtRequest.getStatus());
        court.setDescription(courtRequest.getDescription());
        court.setVenue(existingVenue);
        courtRepository.save(court);

        //cap nhat lai so luong courts cho venue
        int numberOfCourts = courtRepository.countByVenueId(existingVenue.getId());
        existingVenue.setNumberOfCourts(numberOfCourts);
        //lưu số lượng san
        venueRepository.save(existingVenue);
        return court;
    }
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    //lấy court dựa trên id
    public Court getCourtById(long courtId) {
        Court court = courtRepository.findById(courtId).orElseThrow(() -> new BadRequestException("Cpurt not found with Id: "+ courtId));
        if (court == null) {
            throw new BadRequestException("CourtId is not existed: " + courtId);
        }
        return court;
    }


    //show toàn bộ court
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    //update Court
    public Court updateCourt(UpdateCourtRequest courtRequest, long courtId) {
        Venue existingVenue = venueRepository.findById(courtRequest.getVenueId())
                .orElseThrow(()-> new BadRequestException("Venue not found"));
        Court existingCourt = courtRepository.findById(courtId)
                .orElseThrow(()-> new BadRequestException("Court not found "));

        existingCourt.setCourtName(courtRequest.getCourtName());
        existingCourt.setStatus(courtRequest.getStatus());
        existingCourt.setDescription(courtRequest.getDescription());

        existingCourt = courtRepository.save(existingCourt);

        //cap nhat lai so luong courts cho venue
        int numberOfCourts = courtRepository.countByVenueId(existingVenue.getId());
        existingVenue.setNumberOfCourts(numberOfCourts);
        //lưu số lượng san
        venueRepository.save(existingVenue);
        return existingCourt;
    }
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    //delete Court
    public void deActiveCourt(long courtId) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(()->new BadRequestException("Court not found with Id: " + courtId));
        court.setStatus(CourtStatus.INACTIVE);
        courtRepository.save(court);
    }

    public Court activeCourt(long courtId){
        Court court = courtRepository.findById(courtId)
                .orElseThrow(()->new BadRequestException("Court not found with Id: " + courtId));
        court.setStatus(CourtStatus.AVAILABLE);
        return courtRepository.save(court);
    }


}