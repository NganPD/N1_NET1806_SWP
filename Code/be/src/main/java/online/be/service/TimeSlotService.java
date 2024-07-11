package online.be.service;

import online.be.entity.Court;
import online.be.entity.TimeSlot;
import online.be.entity.Venue;
import online.be.exception.BadRequestException;
import online.be.exception.ResourceNotFoundException;
import online.be.model.Request.TimeSlotRequest;
import online.be.model.Response.TimeSlotResponse;
import online.be.model.SlotIdCountDTO;
import online.be.repository.CourtRepository;
import online.be.repository.TimeSlotRepository;
import online.be.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    CourtRepository courtRepo;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // Lưu một TimeSlot mới hoặc cập nhật một TimeSlot đã tồn tại
    public TimeSlot createTimeSlot(TimeSlotRequest timeSlotRequest) {
        LocalTime startTime = LocalTime.parse(timeSlotRequest.getStartTime());
        LocalTime endTime = LocalTime.parse(timeSlotRequest.getEndTime());
        //check the overlapping time slots
        List<TimeSlot> existingSlots = timeSlotRepository.findByVenueId(timeSlotRequest.getVenueId());
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
        timeSlot.setPrice(timeSlotRequest.getPrice());
        timeSlot.setDuration(duration);

        //Set venue
        //check whether the venue exist or not
        Venue venue = venueRepository.findById(timeSlotRequest.getVenueId())
                .orElseThrow(() -> new BadRequestException("The venue cannot be found by ID" + timeSlotRequest.getVenueId()));
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
        long duration = Duration.between(timeSlot.getStartTime(), timeSlot.getEndTime()).toMinutes();
        timeSlot.setDuration(duration);
        timeSlot.setPrice(timeSlotRequest.getPrice());
        venueRepository.findById(timeSlotRequest.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("The venue cannot be found by ID: " + timeSlotRequest.getVenueId()));
        return timeSlotRepository.save(timeSlot);
    }    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Tìm các TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    public List<TimeSlot> findByStartTimeBetween(LocalTime start, LocalTime end) {
        return timeSlotRepository.findByStartTimeBetween(start, end);
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

    // Xoá một TimeSlot
    public void deleteTimeSlot(Long timeSlotId) {
        if (!timeSlotRepository.existsById(timeSlotId)) {
            throw new ResourceNotFoundException("TimeSlot with id " + timeSlotId + " not found");
        }
        timeSlotRepository.deleteById(timeSlotId);
    }

    public TimeSlotResponse mapperSlot(TimeSlot slot){
        TimeSlotResponse slotResponse = new TimeSlotResponse();
        slotResponse.setId(slot.getId());
        slotResponse.setDuration(slot.getDuration());
        slotResponse.setStartTime(String.valueOf(slot.getStartTime()));
        slotResponse.setEndTime(String.valueOf(slot.getEndTime()));
        slotResponse.setAvailable(true);
        return slotResponse;
    }

    public List<TimeSlotResponse> getAvailableSlots(long courtId, LocalDate date){
        Court court = courtRepo.findById(courtId).orElseThrow(() ->
                new RuntimeException("Court not found with id: " + courtId));
        List<TimeSlot> slots = timeSlotRepository.findByVenueId(court.getVenue().getId());
        List<TimeSlotResponse> slotResponses = new ArrayList<>();
        List<SlotIdCountDTO> list = new ArrayList<>();
        List<Object[]> results = timeSlotRepository.countTimeSlotsByCourtIdAndDate(courtId, date);
        list = results.stream()
                .map(result -> new SlotIdCountDTO((Long) result[0], (Long) result[1]))
                .collect(Collectors.toList());
        for (TimeSlot slot: slots){
            TimeSlotResponse slotResponse = mapperSlot(slot);

            if(date.equals(LocalDate.now()) || date.isBefore(LocalDate.now())){
                slotResponse.setAvailable(!isSlotExpired(date, slot.getStartTime()));
            }

            for (SlotIdCountDTO slotIdCountDTO: list){
                if (slot.getId() == slotIdCountDTO.getSlotId() && slotIdCountDTO.getCount() == 1){
                    slotResponse.setAvailable(false);
                }
            }
            slotResponses.add(slotResponse);
        }
        return slotResponses;
    }
    // Check if the slot date and time has already passed
    private boolean isSlotExpired(LocalDate slotDate, LocalTime slotStartTime) {
        try {
            LocalDateTime slotDateTime = LocalDateTime.of(slotDate, slotStartTime);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTime = slotDateTime.minusMinutes(30);
            return now.isAfter(expiryTime);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            // Handle the exception as needed
            return true;
        }
    }

//    // Get TimeSlots by Venue and Exclude Court on Date
//    public List<TimeSlot> getAvailableTimeSlotsWithAtLeastOneCourtInVenue(Long venueId, LocalDate date) {
//        try {
//            return timeSlotRepository.findAvailableTimeSlotsWithAtLeastOneCourtInVenue(venueId, date);
//        } catch (Exception e) {
//            // Log the exception if needed
//            throw new RuntimeException("Failed to fetch time slots by venue and court excluding date: " + e.getMessage(), e);
//        }
//    }
//
//    public long getAvailableTimeSlotCountForVenueOnDate(Long venueId, LocalDate date) {
//        try {
//            List<TimeSlot> list = timeSlotRepository.findAvailableTimeSlotsWithAtLeastOneCourtInVenue(venueId, date);
//
//            // Check if list is null (if findAvailableTimeSlotsWithAtLeastOneCourtInVenue can return null)
//            if (list == null) {
//                return 0;
//            }
//
//            // Return the size of the list directly
//            return list.size();
//        } catch (Exception e) {
//            // Log the exception if needed
//            throw new RuntimeException("Failed to fetch time slots by venue and court for date: " + date, e);
//        }
//    }

    //update trang thai cua timeslot khi dat lich thanh cong
    public TimeSlot updateTimeSlotAvailability(long timeSlotId, boolean isAvailable) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new BadRequestException("Time slot not found"));
        return timeSlotRepository.save(timeSlot);
    }
//
//    //kiem tra timeslot con trong hay khong
//    public boolean isTimeSlotAvailable(long timeSlotId){
//        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
//                .orElseThrow(()-> new BadRequestException("Time slot not found"));
//        return timeSlot.isAvailableStatus();
//    }

//
//    // Get TimeSlots by Venue and Exclude Court on Date
//    public List<TimeSlot> getTimeSlotsByVenueAndCourtExcludingDate(Long venueId, Long courtId, LocalDate date) {
//        try {
//            return timeSlotRepository.findTimeSlotsByVenueAndCourtExcludingDate(venueId, courtId, date);
//        } catch (Exception e) {
//            // Log the exception if needed
//            throw new RuntimeException("Failed to fetch time slots by venue and court excluding date: " + e.getMessage(), e);
//        }
//    }
}