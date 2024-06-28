package online.be.service;

import online.be.entity.Account;
import online.be.entity.Court;
import online.be.entity.Venue;
import online.be.enums.VenueStatus;
import online.be.exception.BadRequestException;
import online.be.exception.NoDataFoundException;
import online.be.model.Request.CreateVenueRequest;
import online.be.model.Request.UpdateVenueRequest;
import online.be.repository.AccountRepostory;
import online.be.repository.CourtRepository;
import online.be.repository.PaymentAccountRepository;
import online.be.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class VenueService {

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    CourtRepository courtRepository;

    @Autowired
    PaymentAccountRepository paymentAccountRepository;

    @Autowired
    AccountRepostory accountRepostory;

    // Tạo một venue mới
    public Venue createVenue(CreateVenueRequest createVenueRequest) {
        //kiểm tra format tên của venue
        if (!Pattern.matches("^[a-zA-Z\\s]+$", createVenueRequest.getVenueName())) {
            throw new RuntimeException("Venue name contains invalid characters. Only letters and spaces are allowed.");
        }
        //kiểm tra xem sân tồn tại bên trong hệ thống chưa
        Venue existingVenue = venueRepository.findByName(createVenueRequest.getVenueName());
        if (existingVenue != null) {
            throw new DataIntegrityViolationException("Duplicate venue");
        }
        //nếu venue chưa tồn tại trong hệ thống
        Venue venue = new Venue();
        venue.setName(createVenueRequest.getVenueName());
        venue.setAddress(createVenueRequest.getAddress());
        venue.setDescription(createVenueRequest.getDescription());
        venue.setOperatingHours(createVenueRequest.getOperatingHours());
        venue.setClosingHours(createVenueRequest.getClosingHours());
        venue.setVenueStatus(createVenueRequest.getVenueStatus());

        //Nếu có chỉ định manager, thì add id manager vào venue
        if (createVenueRequest.getManagerId() != null) {
            Account manager = accountRepostory.findById(createVenueRequest.getManagerId())
                    .orElseThrow(() -> new BadRequestException("Manager account not found"));
            venue.setManager(manager);
        }
        //save venue informtion down to database
        try {
            venue = venueRepository.save(venue);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new BadRequestException("Failed to register venue");
        }
        return venue;

    }//Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Lấy venue bằng ID
    public Venue getVenueById(long venueId) {
        // Sử dụng findById của repository để tìm venue theo ID
        Venue venue = venueRepository.findById(venueId).get();
        if (venue == null) {
            try {
                throw new BadRequestException("Venue not found");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
        return venue;
        //Tự handle lỗi để front end nhận được
    }

    // Lấy tất cả venues
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    // Cập nhật thông tin venue
    public Venue updateVenue(long venueId, UpdateVenueRequest updateVenueRequest) {
        //kiem tra ten venue
        if (!Pattern.matches("^[a-zA-Z\\s]+$", updateVenueRequest.getVenueName())) {
            throw new RuntimeException("Venue name contains invalid characters. Only letters and spaces are allowed.");
        }
        // Find venue by ID
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new BadRequestException("Venue not found with id: " + venueId));
        // Update venue information based on the request
        venue.setName(updateVenueRequest.getVenueName());
        venue.setAddress(updateVenueRequest.getAddress());
        venue.setDescription(updateVenueRequest.getDescription());
        venue.setOperatingHours(updateVenueRequest.getOperatingHours());
        venue.setClosingHours(updateVenueRequest.getClosingHours());
        venue.setVenueStatus(updateVenueRequest.getVenueStatus());

        //If assigned courts are updated, handle the assignment
        if (updateVenueRequest.getAssignedCourts() != null) {
            List<Court> courts = courtRepository.findAllById(updateVenueRequest.getAssignedCourts());
            venue.setCourts(courts);
        }
        try {
            // Lưu và trả về venue đã được cập nhật
            venue = venueRepository.save(venue);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Failed to save venue information: " + e.getMessage());
        }
        return venue;
    }

    // Xóa venue bằng ID
    public void deleteVenue(long venueId) {
        // Trước tiên kiểm tra xem venue có tồn tại hay không
        Venue existingVenue = venueRepository.findById(venueId)
                .orElseThrow(() -> new BadRequestException("Venue not found with ID: " + venueId));
        //Tự handle lỗi để front end nhận được
        existingVenue.setVenueStatus(VenueStatus.INACTIVE);
    }

    //search by name
    public List<Venue> searchvenues(String keywords) {
        List<Venue> venueList = venueRepository.searchVenue(keywords);
        if (venueList.isEmpty()) {
            throw new NoDataFoundException("0 search");
        } else {
            return venueList;
        }
    }

    //search by operating hours
    public List<Venue> searchVenuesByOperatingHours(String operaingHours) {
        return venueRepository.findByOperatingHours(operaingHours);
    }

    //
////    //search by opening hours and closing hours
////    public List<Venue> getByOpeningHour(String openingHour){
////        List<Venue> venues = venueRepository.findByOpeningHours(openingHour);
////        if(venues.isEmpty()){
////            throw new RuntimeException("No Valid Data");
////        }
////        return venues;
////    }
//
    //search by Location
    //search by available slots
    public List<Venue> getVenueWithAvailableSlots(LocalTime startTime, LocalTime endTime) {
        List<Venue> venues = venueRepository.findVenueWithAvailableSlots(startTime, endTime);
        if (venues.isEmpty()) {
            throw new NoDataFoundException("0 search");
        }
        return venues;
    }
}

