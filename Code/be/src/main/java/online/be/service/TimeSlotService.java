package online.be.service;

import online.be.entity.TimeSlot;
import online.be.entity.Venue;
import online.be.exception.BadRequestException;
import online.be.exception.ResourceNotFoundException;
import online.be.model.Request.TimeSlotRequest;
import online.be.repository.TimeSlotRepository;
import online.be.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private VenueRepository venueRepository;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    // Lưu một TimeSlot mới hoặc cập nhật một TimeSlot đã tồn tại
    public TimeSlot createTimeSlot(TimeSlotRequest timeSlotRequest) {
        if (timeSlotRequest.getStartTime() == null || timeSlotRequest.getEndTime() == null) {
            throw new BadRequestException("Start time and end time are required");
        }
//        //validate time range
//        if(timeSlotRequest.getStartTime().isAfter(timeSlotRequest.getEndTime())){
//            throw new BadRequestException("Start time must be before end time");
//        }
//
//        //check the overlapping time slots
//        List<TimeSlot> existingSlots = timeSlotRepository.findByVenueVenueId(timeSlotRequest.getVenueId());
//        for(TimeSlot slot : existingSlots){
//            if(timeSlotRequest.getStartTime().isBefore(slot.getStartTime())
//                    && timeSlotRequest.getEndTime().isAfter(slot.getStartTime())){
//                throw new BadRequestException("The time slot overlaps with an existing time slot");
//            }
//


        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(LocalTime.parse(timeSlotRequest.getStartTime()));
        timeSlot.setEndTime(LocalTime.parse(timeSlotRequest.getEndTime()));
        timeSlot.setPrice(timeSlotRequest.getPrice());
        timeSlot.setAvailableStatus(timeSlotRequest.isStatus());

        //Set venue
        Venue venue = venueRepository.findById(timeSlotRequest.getVenueId())
                .orElseThrow(()-> new BadRequestException("The venue cannot be found by ID" + timeSlotRequest.getVenueId()));
        timeSlot.setVenue(venue);

        //calculate and set duration
        int duration = (int) Duration.between(timeSlot.getStartTime(), timeSlot.getEndTime()).toMinutes();
        timeSlot.setDuration(duration);
        return timeSlotRepository.save(timeSlot);
    }    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Cập nhật thông tin TimeSlot
    public TimeSlot updateTimeSlot(long timeSlotId, TimeSlotRequest timeSlotRequest) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(()-> new ResourceNotFoundException("Time slot cannot be found by ID: " + timeSlotId));

//        timeSlot.setDuration(timeSlotRequest.getDuration());
        timeSlot.setStartTime(LocalTime.parse(timeSlotRequest.getStartTime()));
        timeSlot.setEndTime(LocalTime.parse(timeSlotRequest.getEndTime()));
        timeSlot.setPrice(timeSlotRequest.getPrice());
        timeSlot.setAvailableStatus(timeSlot.isAvailableStatus());

        int duration = (int) Duration.between(timeSlot.getStartTime(), timeSlot.getEndTime()).toMinutes();
        timeSlot.setDuration(duration);
        venueRepository.findById(timeSlotRequest.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("The venue cannot be found by ID: " + timeSlotRequest.getVenueId()));
        return timeSlotRepository.save(timeSlot);
    }    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Tìm các TimeSlot theo độ dài
    public List<TimeSlot> findByDuration(int duration) {
        return timeSlotRepository.findByDuration(duration);
    }//Tự tạo hiển thị không có slot

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
    public TimeSlot updateTimeSlotAvailability(long timeSlotId, boolean isAvailable){
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(()-> new BadRequestException("Time slot not found"));
        timeSlot.setAvailableStatus(isAvailable);
        return timeSlotRepository.save(timeSlot);
    }

    //kiem tra timeslot con trong hay khong
    public boolean isTimeSlotAvailable(long timeSlotId){
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(()-> new BadRequestException("Time slot not found"));
        return timeSlot.isAvailableStatus();
    }

}