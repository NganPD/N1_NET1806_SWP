package online.be.service;

import online.be.entity.Venue;
import online.be.enums.VenueStatus;
import online.be.exception.BadRequestException;
import online.be.model.Request.VenueRequest;
import online.be.repository.PaymentAccountRepository;
import online.be.repository.VenueRepostiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class VenueService {

    @Autowired
    VenueRepostiory venueRepostiory;

    @Autowired
    PaymentAccountRepository paymentAccountRepository;

    // Tạo một venue mới
    public Venue createVenue(VenueRequest venueRequest) {
        if(!Pattern.matches("^[a-zA-Z\\s]+$", venueRequest.getVenueName())){
            throw new RuntimeException("Venue name contains invalid characters. Only letters and spaces are allowed.");
        }
        Venue venue = new Venue();
        venue.setName(venueRequest.getVenueName());
        venue.setAddress(venueRequest.getAddress());
        venue.setDescription(venueRequest.getDescription());
        venue.setOperatingHours(venueRequest.getOperatingHours());
        venue.setClosingHours(venue.getClosingHours());
        venue.setPaymentAccount(paymentAccountRepository.findById(venueRequest.getPaymentAccountId()).get());
        try {
            venue = venueRepostiory.save(venue);
        }catch(DataIntegrityViolationException e){
            System.out.println(e.getMessage());
        }
        return venue;

    }//Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Lấy venue bằng ID
    public Venue getVenueById(long venueId) {
        // Sử dụng findById của repository để tìm venue theo ID
        Venue venue = venueRepostiory.findById(venueId).get();
        if(venue == null){
            try{
                throw new BadRequestException("Venue not found");
            }catch (BadRequestException e){
                throw new RuntimeException(e);
            }
        }
        return venue;
        //Tự handle lỗi để front end nhận được
    }

    // Lấy tất cả venues
    public List<Venue> getAllVenues() {
        return venueRepostiory.findAll();
    }

    // Cập nhật thông tin venue
<<<<<<< HEAD
    public Venue updateVenue(long venueId, VenueRequest venueRequest) {
        //kiem tra ten venue
        if(!Pattern.matches("^[a-zA-Z\\s]+$", venueRequest.getVenueName())){
            throw new RuntimeException("Venue name contains invalid characters. Only letters and spaces are allowed.");
        }
        // Lấy venue bằng ID
        Venue venue = venueRepostiory.findById(venueId)
                .orElseThrow(()-> new BadRequestException("Venue not found with id: " + venueId));
        // Cập nhật thông tin venue với thông tin mới từ venueRequest
        venue.setName(venueRequest.getVenueName());
        venue.setAddress(venueRequest.getAddress());
        venue.setDescription(venueRequest.getDescription());
        venue.setOperatingHours(venueRequest.getOperatingHours());
        venue.setPaymentAccount(paymentAccountRepository.findById(venueRequest.getPaymentAccountId()).get());
        //cập nhật lại danh sách court trong venue
        try{
            // Lưu và trả về venue đã được cập nhật
            venue = venueRepostiory.save(venue);
        }catch (DataIntegrityViolationException e){
            System.out.println(e.getMessage());
        }
        return venue;

=======
    public Venue updateVenue(long venueId, VenueRequest venueDetails) {
        // Lấy venue bằng ID
        Venue venue = getVenueById(venueId);
        // Cập nhật thông tin venue với thông tin mới từ venueDetails
        venue.setName(venueDetails.getVenueName());
        venue.setAddress(venueDetails.getAddress());
        venue.setDescription(venueDetails.getDescription());
        venue.setOperatingHours(venueDetails.getOperatingHours());
        venue.setPaymentInfor(venueDetails.getPaymentInfor());
        venue.setNumberOfCourts(venueDetails.getNumberOfCourts());
        venue.setImageURL(venueDetails.getImageURL());
        venue.setCourts((List<Court>) venueDetails.getCourt());
        // Lưu và trả về venue đã được cập nhật
        return venueRepostiory.save(venue);
>>>>>>> origin/feat/AccountManagerAPI
    }//Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Xóa venue bằng ID
    public void deleteVenue(long venueId) {
        // Trước tiên kiểm tra xem venue có tồn tại hay không
        Venue existingVenue = venueRepostiory.findById(venueId)
                .orElseThrow(() -> new BadRequestException("Venue not found with ID: " + venueId));
        //Tự handle lỗi để front end nhận được
        existingVenue.setVenueStatus(VenueStatus.INACTIVE);
    }

    //search by name
    public List<Venue> getByName(String venueName){
        List<Venue> venueList = venueRepostiory.findByName(venueName);
        if(venueList.isEmpty()){
            throw new RuntimeException("No Data");
        }else{
            return venueList;
        }
    }

    //search by opening hours and closing hours
    public List<Venue> getByOpeningHour(String openingHour){
        List<Venue> venues = venueRepostiory.findByOpeningHours(openingHour);
        if(venues.isEmpty()){
            throw new RuntimeException("No Valid Data");
        }
        return venues;
    }

    //search by Location
    //search by available slots
    public List<Venue> getVenueWithAvailableSlots(LocalDateTime startDateTime, LocalDateTime endDateTime){
        return venueRepostiory.findVenueWithAvailableSlots(startDateTime,endDateTime);
    }

}