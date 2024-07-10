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
        //Find the venue ỏ throw exception if not found
        Venue existingVenue = venueRepository.findById(courtRequest.getVenueId())
                .orElseThrow(()-> new BadRequestException("The venue does not exist"));

        //Create a new Court
        Court court = new Court();
        court.setCourtName(courtRequest.getCourtName());
        court.setStatus(courtRequest.getStatus());
        court.setDescription(courtRequest.getDescription());

        court.setVenue(existingVenue);
        return courtRepository.save(court);
    }
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    //lấy court dựa trên id
    public Court getCourtById(long courtId) {
        Court court = courtRepository.findById(courtId).get();
        if (court == null) {
            throw new BadRequestException("CourtId is not existed: " + courtId);
        }
        return court;
    }

    public List<Court> getCourtByVenueId(long venueId) {
        List<Court> courts = courtRepository.findByVenueId(venueId);
        if (courts == null) {
            throw new BadRequestException("Có con cak mà đòi");
        }
        return courts;
    }


    //show toàn bộ court
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    //update Court
    public Court updateCourt(UpdateCourtRequest courtRequest, long courtId) {
        Court existingCourt = courtRepository.findById(courtId)
                .orElseThrow(()-> new RuntimeException("Court not found "));

        existingCourt.setCourtName(courtRequest.getCourtName());
        existingCourt.setStatus(courtRequest.getStatus());
        existingCourt.setDescription(courtRequest.getDescription());

        return courtRepository.save(existingCourt);
    }
    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    //delete Court
    public void deleteCourt(Long courtId) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(()->new RuntimeException("Court not found with Id: " + courtId));
        court.setStatus(CourtStatus.INACTIVE);
        courtRepository.save(court);
    }


}