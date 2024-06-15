package online.be.service;

import online.be.entity.Account;
import online.be.entity.Court;
import online.be.entity.Venue;
import online.be.model.Request.VenueRequest;
import online.be.repository.AuthenticationRepository;
import online.be.repository.CourtRepository;
import online.be.repository.VenueRepostiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class VenueService {

    @Autowired
    VenueRepostiory venueRepostiory;
    @Autowired
    CourtRepository courtRepository;
    @Autowired
    AuthenticationRepository authenticationRepository;

    // Tạo một venue mới
    public Venue createVenue(VenueRequest venueRequest) {
        Venue venue = new Venue();
        venue.setName(venueRequest.getVenueName());
        venue.setDescription(venueRequest.getDescription());
        venue.setImageURL(venueRequest.getImageURL());
        venue.setPaymentInfor(venueRequest.getPaymentInfor());
        venue.setNumberOfCourts(venueRequest.getNumberOfCourts());
        venue.setOperatingHours(venueRequest.getOperatingHours());
        venue.setClosingHours(venue.getClosingHours());
        //kiem tra cac danh sach id cua court
        List<Court> courts = courtRepository.findAllById(venueRequest.getCourtId());
        if(courts != null){
            //them vao venue
            venue.setCourts(courts);
        }
        Account manager = authenticationRepository.findById(venueRequest.getManagerId()).get();
        if(manager != null){
            //them manager vao thong tin cua venue
            venue.setManager(manager);
        }
        return venueRepostiory.save(venue);
    }//Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Lấy venue bằng ID
    public Venue getVenueById(long venueId) {
        // Sử dụng findById của repository để tìm venue theo ID
        // Nếu không tìm thấy, ném ra một RuntimeException
        return venueRepostiory.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found with ID: " + venueId));//Tự handle lỗi để front end nhận được
    }

    // Lấy tất cả venues
    public List<Venue> getAllVenues() {
        return venueRepostiory.findAll();
    }

    // Cập nhật thông tin venue
    public Venue updateVenue(long venueId, VenueRequest venueRequest) {
        // Lấy venue bằng ID
        Venue venue = venueRepostiory.findById(venueId).get();
        // Cập nhật thông tin venue với thông tin mới từ venueRequest
        venue.setName(venueRequest.getVenueName());
        venue.setAddress(venueRequest.getAddress());
        venue.setDescription(venueRequest.getDescription());
        venue.setOperatingHours(venueRequest.getOperatingHours());
        venue.setPaymentInfor(venueRequest.getPaymentInfor());
        //cập nhật lại danh sách court trong venue

        // Lưu và trả về venue đã được cập nhật
        return venueRepostiory.save(venue);
    }//Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Xóa venue bằng ID
    public void deleteVenue(long venueId) {
        // Trước tiên kiểm tra xem venue có tồn tại hay không
        if (!venueRepostiory.existsById(venueId)) {
            throw new RuntimeException("Venue not found with ID: " + venueId);//Tự handle lỗi để front end nhận được
        }
        // Nếu tồn tại, thực hiện xóa venue

    }
}