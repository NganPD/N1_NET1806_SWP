package online.be.service;

import online.be.entity.Venue;
import online.be.repository.VenueRepostiory;

import java.util.List;

public class VenueService {

    private final VenueRepostiory venueRepostiory;

    // Constructor
    public VenueService(VenueRepostiory venueRepostiory) {
        this.venueRepostiory = venueRepostiory;
    }

    // Tạo một venue mới
    public Venue createVenue(Venue venue) {
        return venueRepostiory.save(venue);
    }

    // Lấy venue bằng ID
    public Venue getVenueById(Long venueId) {
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
    public Venue updateVenue(Long venueId, Venue venueDetails) {
        // Lấy venue bằng ID
        Venue venue = getVenueById(venueId);
        // Cập nhật thông tin venue với thông tin mới từ venueDetails
        venue.setName(venueDetails.getName());
        venue.setAddress(venueDetails.getAddress());
        venue.setDescription(venueDetails.getDescription());
        venue.setOperatingHours(venueDetails.getOperatingHours());
        venue.setPaymentInfor(venueDetails.getPaymentInfor());
        venue.setNumberOfCourts(venueDetails.getNumberOfCourts());
        venue.setRating(venueDetails.getRating());
        venue.setImageURL(venueDetails.getImageURL());
        // Lưu và trả về venue đã được cập nhật
        return venueRepostiory.save(venue);
    }

    // Xóa venue bằng ID
    public void deleteVenue(Long venueId) {
        // Trước tiên kiểm tra xem venue có tồn tại hay không
        if (!venueRepostiory.existsById(venueId)) {
            throw new RuntimeException("Venue not found with ID: " + venueId);
        }
        // Nếu tồn tại, thực hiện xóa venue
        venueRepostiory.deleteById(venueId);
    }
}