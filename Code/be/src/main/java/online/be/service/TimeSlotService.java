package online.be.service;

import online.be.entity.Account;
import online.be.entity.Court;
import online.be.entity.TimeSlot;
import online.be.entity.Venue;
import online.be.exception.BadRequestException;
import online.be.exception.ResourceNotFoundException;
import online.be.model.Request.TimeSlotRequest;
import online.be.model.Response.TimeSlotResponse;
import online.be.model.SlotIdCountDTO;
import online.be.repository.CourtRepository;
import online.be.repository.CourtTimeSlotRepository;
import online.be.repository.TimeSlotRepository;
import online.be.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeSlotService {

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    CourtRepository courtRepo;

    @Autowired
    CourtTimeSlotRepository courtTimeSlotRepository;

    @Autowired
    AuthenticationService authenticationService;

    // Lưu một TimeSlot mới hoặc cập nhật một TimeSlot đã tồn tại
    public TimeSlot createTimeSlot(TimeSlotRequest timeSlotRequest) {
        LocalTime startTime = LocalTime.parse(timeSlotRequest.getStartTime());
        LocalTime endTime = LocalTime.parse(timeSlotRequest.getEndTime());
        //check the overlapping time slots
        Account manager =  authenticationService.getCurrentAccount();
        Venue venue = manager.getAssignedVenue();
        if(venue == null){
            throw new BadRequestException("Venue not found");
        }
        List<TimeSlot> existingSlots = timeSlotRepository.findByVenueId(venue.getId());
        for (TimeSlot slot : existingSlots) {
            if (startTime.isBefore(slot.getStartTime())
                    && endTime.isAfter(slot.getStartTime())) {
                throw new BadRequestException("The time slot overlaps with an existing time slot");
            }
        }
        //calculate duration
        long duration = Duration.between(startTime, endTime).toHours();
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(LocalTime.parse(timeSlotRequest.getStartTime()));
        timeSlot.setEndTime(LocalTime.parse(timeSlotRequest.getEndTime()));
        timeSlot.setDuration(duration);

        //Set venue
        //check whether the venue exist or not
        timeSlot.setVenue(venue);
        return timeSlotRepository.save(timeSlot);
    }    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Cập nhật thông tin TimeSlot
    public TimeSlot updateTimeSlot(long timeSlotId, TimeSlotRequest timeSlotRequest) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new ResourceNotFoundException("Time slot cannot be found by ID: " + timeSlotId));

//        timeSlot.setDuration(timeSlotRequest.getDuration());
        timeSlot.setStartTime(LocalTime.parse(timeSlotRequest.getStartTime()));
        timeSlot.setEndTime(LocalTime.parse(timeSlotRequest.getEndTime()));
        long duration = Duration.between(timeSlot.getStartTime(), timeSlot.getEndTime()).toHours();
        timeSlot.setDuration(duration);

        return timeSlotRepository.save(timeSlot);
    }    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Tìm các TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    public List<TimeSlot> findByStartTimeBetween(String start, String end) {
        return timeSlotRepository.findByStartTimeBetween(LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm")), LocalTime.parse(end, DateTimeFormatter.ofPattern("HH:mm")));
    }//Tự tạo hiển thị không có slot

    // Read TimeSlot by ID
    public TimeSlot getTimeSlotById(long timeSlotId) {
        return timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new ResourceNotFoundException("TimeSlot cannot found by ID: " + timeSlotId));
    }

    public List<TimeSlot> getAllTimeSlots() {
        try {
            List<TimeSlot> timeSlots = timeSlotRepository.findAll();
            if (timeSlots.isEmpty()) {
                throw new ResourceNotFoundException("No time slots found");
            }
            return timeSlots;
        } catch (Exception e) {
            // Log the exception if necessary
            throw new RuntimeException("An error occurred while fetching time slots: " + e.getMessage(), e);
        }
    }
    public TimeSlotResponse mapperSlot(TimeSlot slot) {
        TimeSlotResponse slotResponse = new TimeSlotResponse();
        slotResponse.setId(slot.getId());
        slotResponse.setDuration(slot.getDuration());
        slotResponse.setStartTime(String.valueOf(slot.getStartTime()));
        slotResponse.setEndTime(String.valueOf(slot.getEndTime()));
        slotResponse.setAvailable(true);
        return slotResponse;
    }

    public List<TimeSlotResponse> getAvailableSlots(String date, long venueId) {
        List<TimeSlot> slots = timeSlotRepository.getTimeSlotByVenue_Id(venueId);
        List<TimeSlotResponse> slotResponses = new ArrayList<>();
        LocalDate checkInDate = null;

        if (date != null && !date.isEmpty()) {
            checkInDate = LocalDate.parse(date);
        }

        for (TimeSlot slot : slots) {
            TimeSlotResponse slotResponse = mapperSlot(slot);
            if (checkInDate != null) {
                if (checkInDate.equals(LocalDate.now()) || checkInDate.isBefore(LocalDate.now())) {
                    slotResponse.setAvailable(isSlotExpired(checkInDate, slot.getStartTime()));
                }
                slotResponses.add(slotResponse);
            } else {
                slotResponses.add(slotResponse);
            }
        }

        return slotResponses;
    }

    // Method to get all slots of a venue
    private List<TimeSlot> getAllSlots() {
        return timeSlotRepository.findAll(); // Assumes timeSlotRepository is the repository for TimeSlot
    }

    // New method to get all slots by venue
    public List<TimeSlot> getAllSlotByVenue(long venueId) {
        return timeSlotRepository.findByVenueId(venueId);
    }

    // Check if the slot date and time has already passed
    private boolean isSlotExpired(LocalDate slotDate, LocalTime slotStartTime) {
        try {
            LocalDateTime slotDateTime = LocalDateTime.of(slotDate, slotStartTime);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTime = slotDateTime.minusMinutes(60);
            return !now.isAfter(expiryTime);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
