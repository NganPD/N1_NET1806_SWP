package online.be.service;

import online.be.entity.*;
import online.be.enums.BookingType;
import online.be.enums.VenueStatus;
import online.be.exception.BadRequestException;
import online.be.exception.NoDataFoundException;
import online.be.exception.VenueException;
import online.be.model.Request.CreateVenueRequest;
import online.be.model.Request.UpdateVenueRequest;
import online.be.model.Response.TimeSlotResponse;
import online.be.model.Response.VenueResponse;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class VenueService {

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    CourtRepository courtRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    TimeSlotService timeSlotService;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // Tạo một venue
    public Venue createVenue(CreateVenueRequest createVenueRequest) {
        //chuyển đổi request string thành localtime
        LocalTime openingHour = LocalTime.parse(createVenueRequest.getOpeningHours(), timeFormatter);
        LocalTime closingHour = LocalTime.parse(createVenueRequest.getClosingHours(), timeFormatter);
        //kiểm tra xem sân tồn tại bên trong hệ thống chưa
        Venue existingVenue = venueRepository.findByName(createVenueRequest.getVenueName());
        if (existingVenue != null) {
            throw new VenueException("Duplicate venue");
        }
        //nếu venue chưa tồn tại trong hệ thống
        Account manager = accountRepository.findUserById(createVenueRequest.getManagerId());
        if (manager == null) {
            throw new BadRequestException("Manager not found");
        }
        Venue venue = new Venue();
        venue.setName(createVenueRequest.getVenueName());
        venue.setAddress(createVenueRequest.getAddress());
        venue.setContactInfor(createVenueRequest.getContactInfor());
        venue.setDescription(createVenueRequest.getDescription());
        venue.setOpeningHour(openingHour);
        venue.setClosingHour(closingHour);
        venue.setServices(createVenueRequest.getServices());
        venue.setVenueStatus(createVenueRequest.getVenueStatus());
        venue.setManager(manager);
        //save venue informtion down to database
        try {
            venue = venueRepository.save(venue);
            //tính số lượng sân
            int numberOfCourts = courtRepository.countByVenueId(venue.getId());
            //cập nhật số lượng sân nhỏ
            venue.setNumberOfCourts(numberOfCourts);
            //lưu lại venue
            venue = venueRepository.save(venue);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new VenueException("Failed to register venue");
        }
        return venue;

    }//Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi


    // Lấy venue bằng ID
    public VenueResponse getVenueById(long venueId) {
        // Sử dụng findById của repository để tìm venue theo ID
        Venue venue = venueRepository.findById(venueId).orElseThrow(()
        -> new BadRequestException("Venue cannot found with Id: "+venueId));
        if (venue == null) {
            try {
                throw new VenueException("Venue not found");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
        return mapToVenueResponse(venue);
        //Tự handle lỗi để front end nhận được
    }

    // Lấy tất cả venues
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    // Cập nhật thông tin venue
    public Venue updateVenue(long venueId, UpdateVenueRequest updateVenueRequest) {
        // Find venue by ID
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new BadRequestException("Venue not found with id: " + venueId));
        // Update venue information based on the request
        venue.setName(updateVenueRequest.getVenueName());
        venue.setAddress(updateVenueRequest.getAddress());
        venue.setDescription(updateVenueRequest.getDescription());
        venue.setContactInfor(updateVenueRequest.getContactInfor());
        venue.setOpeningHour(LocalTime.parse(updateVenueRequest.getOperatingHours(), timeFormatter));
        venue.setClosingHour(LocalTime.parse(updateVenueRequest.getClosingHours(), timeFormatter));
        venue.setVenueStatus(updateVenueRequest.getVenueStatus());
        try {
            // Lưu và trả về venue đã được cập nhật
            venue = venueRepository.save(venue);
            //tính số lượng sân
            int numberOfCourts = courtRepository.countByVenueId(venue.getId());
            //cập nhật số lượng sân nhỏ
            venue.setNumberOfCourts(numberOfCourts);
            //lưu lại venue
            venue = venueRepository.save(venue);
        } catch (DataIntegrityViolationException e) {
            throw new VenueException("Failed to save venue information: " + e.getMessage());
        }
        return venue;
    }

    // Xóa venue bằng ID
    public void deleteVenue(long venueId) {
        // Trước tiên kiểm tra xem venue có tồn tại hay không
        Venue existingVenue = venueRepository.findById(venueId)
                .orElseThrow(() -> new BadRequestException("Venue not found with ID: " + venueId));
        existingVenue.setVenueStatus(VenueStatus.CLOSE);
        venueRepository.save(existingVenue);//lưu thay đổi
    }

    //search by name
    public List<Venue> searchVenuesByKeyword(String keywords) {
        List<Venue> venueList = venueRepository.findVenueByKeywords(keywords);
        if (venueList.isEmpty()) {
            throw new NoDataFoundException("0 search");
        } else {
            return venueList;
        }
    }

    public List<VenueResponse> searchVenues(String operatingHoursStr, String location, String timeStr) {
        List<Venue> venues = venueRepository.findAll();
        List<VenueResponse> venueResponses = new ArrayList<>();

        LocalDate currentDateTime = LocalDate.now();

        for (Venue venue : venues) {
            boolean matchesOperatingHours = true;
            boolean matchesLocation = true;
            boolean matchesAvailableSlot = true;

            if (operatingHoursStr != null && !operatingHoursStr.isEmpty()) {
                matchesOperatingHours = venue.getOpeningHour().toString().contains(operatingHoursStr);
            }

            if (location != null && !location.isEmpty()) {
                matchesLocation = venue.getAddress().toLowerCase().contains(location.toLowerCase());
            }

            if (timeStr != null && !timeStr.isEmpty()) {
                matchesAvailableSlot = false;
                for (Court court : venue.getCourts()) {
                    List<TimeSlotResponse> availableSlots = timeSlotService.getAvailableSlots(court.getId(), String.valueOf(currentDateTime), venue.getId());
                    for (TimeSlotResponse slot : availableSlots) {
                        if (slot.isAvailable() && slot.getStartTime().equals(timeStr)) {
                            matchesAvailableSlot = true;
                            break;
                        }
                    }
                    if (matchesAvailableSlot) {
                        break;
                    }
                }
            }

            if (matchesOperatingHours && matchesLocation && matchesAvailableSlot) {
                venueResponses.add(mapToVenueResponse(venue));
            }
        }


        return venueResponses;
    }

    public Venue addStaffToVenue(long staffId, long venueId) {
        try {
            Venue venue = venueRepository.findVenueById(venueId);
            Account staff = accountRepository.findUserById(staffId);

            //add staff to venue
            staff.setStaffVenue(venue);
            venue.getStaffs().add(staff);

            //save change
            accountRepository.save(staff);
            venueRepository.save(venue);
            return venue;
        } catch (Exception e) {
            throw new BadRequestException("Error: " + e.getMessage());
        }
    }

    public Account getManager(long venueId) {
        return accountRepository.findManagerByAssignedVenue_Id(venueId);
    }

    public List<Account> getStaffsByVenueId(long venueId) {
        return accountRepository.findStaffByStaffVenue_Id(venueId);
    }

    public List<Court> getCourtByVenueId(long venueId) {
        Venue venue = venueRepository.findVenueById(venueId);
        if (venue == null) {
            throw new NoDataFoundException("Venue not found");
        }
        return courtRepository.findByVenue(venue);
    }

    public List<Review> getReviewByVenueId(long venueId) {
        Venue venue = venueRepository.findVenueById(venueId);
        if (venue == null) {
            throw new NoDataFoundException("Venue not found");
        }
        List<Review> reviews = reviewRepository.findByVenue(venue);
        if (reviews.isEmpty()) {
            throw new NoDataFoundException("0 search");
        }
        return reviews;
    }

    public int numberOfVenues(){
        List<Venue> venueList = venueRepository.findAll();
        return venueList.size();
    }

    private VenueResponse mapToVenueResponse(Venue venue) {
        VenueResponse venueResponse = new VenueResponse();

        // Copy fields from Venue to VenueResponse
        venueResponse.setId(venue.getId());
        venueResponse.setName(venue.getName());
        venueResponse.setAddress(venue.getAddress());
        venueResponse.setContactInfor(venue.getContactInfor());
        venueResponse.setVenueStatus(venue.getVenueStatus());
        venueResponse.setServices(venue.getServices());
        venueResponse.setDescription(venue.getDescription());
        venueResponse.setCourts(venue.getCourts());
        venueResponse.setClosingHour(venue.getClosingHour());
        venueResponse.setOpeningHour(venue.getOpeningHour());
        venueResponse.setPricingList(venue.getPricingList());
        // Additional fields for VenueResponse
        venueResponse.setOperatingHours(
                venue.getOpeningHour().toString() + " - " + venue.getClosingHour().toString()
        );
        venueResponse.setNumberOfCourt(venue.getCourts().size());

        // Assume the price is calculated based on some business logic; here it's a placeholder
        double fixedPrice = getPricingForScheduleType(venue, BookingType.FIXED);
        double dailyPrice = getPricingForScheduleType(venue, BookingType.DAILY);
        double flexiblePrice = getPricingForScheduleType(venue, BookingType.FLEXIBLE);

        venueResponse.setDailyPrice(dailyPrice);
        venueResponse.setFixedPrice(fixedPrice);
        venueResponse.setFlexiblePrice(flexiblePrice);

        double rating = 0;
        List<Review> reviews = venue.getReviews();
        for (Review review : reviews){
            rating += rating;
        }
        venueResponse.setRating(rating/reviews.size());
        return venueResponse;
    }

    private double getPricingForScheduleType(Venue venue, BookingType bookingType) {
        List<Pricing> pricings = new ArrayList<>();
        for (Pricing p : venue.getPricingList()) {
            if (p.getBookingType().equals(bookingType)) {
                pricings.add(p);
            }
        }
        double totalPrice = 0;
        for (Pricing pricing : pricings) {
            totalPrice += pricing.getPricePerHour();  // Hoặc phương thức tính giá khác
        }
        return pricings.isEmpty() ? 0 : totalPrice / pricings.size(); // Tính giá trung bình
    }

    public VenueResponse getVenueByManagerId(long managerId){
        Venue venue = venueRepository.findVenueByManagerId(managerId);
        if(venue == null){
            throw new BadRequestException("Not found manager");
        }
        VenueResponse response = mapToVenueResponse(venue);
        return response;
    }
}

