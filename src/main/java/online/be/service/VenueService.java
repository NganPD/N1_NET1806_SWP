package online.be.service;

import online.be.entity.Court;
import online.be.entity.Venue;
import online.be.model.Request.CreateVenueRequest;
import online.be.repository.CourtRepository;
import online.be.repository.VenueRepostiory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class VenueService {

    VenueRepostiory venueRepostiory;
    CourtRepository courtRepository;

    // Constructor
    public VenueService(VenueRepostiory venueRepostiory) {
        this.venueRepostiory = venueRepostiory;
    }

    // Tạo một venue mới
    public Venue createVenue(CreateVenueRequest venueRequest) {
        Venue venue = new Venue();
        venue.setName(venueRequest.getVenueName());
        venue.setDescription(venueRequest.getDescription());
        venue.setImageURL(venueRequest.getImageURL());
        venue.setPaymentInfor(venueRequest.getPaymentInfor());
        venue.setNumberOfCourts(venueRequest.getNumberOfCourts());
        venue.setOperatingHours(venueRequest.getOperatingHours());
        venue.setCourts(venueRequest.getCourt());

        return venueRepostiory.save(venue);
    }

    // Lấy venue bằng ID
    public Venue getVenueById(long venueId) {
        // Sử dụng findById của repository để tìm venue theo ID
        // Nếu không tìm thấy, ném ra một RuntimeException
        return venueRepostiory.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found with ID: " + venueId));
    }

    // Lấy tất cả venues
    public List<Venue> getAllVenues() {
        return venueRepostiory.findAll();
    }

    // Cập nhật thông tin venue
    public Venue updateVenue(long venueId, Venue venueDetails) {
        // Lấy venue bằng ID
        Venue venue = getVenueById(venueId);
        // Cập nhật thông tin venue với thông tin mới từ venueDetails
        venue.setName(venueDetails.getName());
        venue.setAddress(venueDetails.getAddress());
        venue.setDescription(venueDetails.getDescription());
        venue.setOperatingHours(venueDetails.getOperatingHours());
        venue.setPaymentInfor(venueDetails.getPaymentInfor());
        venue.setNumberOfCourts(venueDetails.getNumberOfCourts());
        venue.setImageURL(venueDetails.getImageURL());
        venue.setCourts((List<Court>) venueDetails.getCourts());
        // Lưu và trả về venue đã được cập nhật
        return venueRepostiory.save(venue);
    }

    // Xóa venue bằng ID
    public void deleteVenue(long venueId) {
        // Trước tiên kiểm tra xem venue có tồn tại hay không
        if (!venueRepostiory.existsById(venueId)) {
            throw new RuntimeException("Venue not found with ID: " + venueId);
        }
        // Nếu tồn tại, thực hiện xóa venue
        venueRepostiory.deleteById(venueId);
    }
}