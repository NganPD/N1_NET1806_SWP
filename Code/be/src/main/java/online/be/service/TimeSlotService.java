package online.be.service;

import online.be.entity.CourtSchedule;
import online.be.entity.TimeSlot;
import online.be.entity.Venue;
import online.be.exception.BadRequestException;
import online.be.model.Request.TimeSlotRequest;
import online.be.repository.CourtScheduleRepository;
import online.be.repository.TimeSlotRepository;
import online.be.repository.VenueRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private CourtScheduleRepository courtScheduleRepository;
    @Autowired
    private VenueRepository venueRepository;

    // Lưu một TimeSlot mới hoặc cập nhật một TimeSlot đã tồn tại
    public TimeSlot createTimeSlot(TimeSlotRequest timeSlotRequest) {
        if (timeSlotRequest.getStartTime() == null || timeSlotRequest.getEndTime() == null) {
            throw new BadRequestException("Start time and end time are required");
        }
        //validate time range
        if(timeSlotRequest.getStartTime().isAfter(timeSlotRequest.getEndTime())){
            throw new BadRequestException("Start time must be before end time");
        }

        //check the overlapping time slots
        List<TimeSlot> existingSlots = timeSlotRepository.findByVenueVenueId(timeSlotRequest.getVenueId());
        for(TimeSlot slot : existingSlots){
            if(timeSlotRequest.getStartTime().isBefore(slot.getStartTime())
                && timeSlotRequest.getEndTime().isAfter(slot.getStartTime())){
                throw new BadRequestException("The time slot overlaps with an existing time slot");
            }
        }
        //calculate duration
        long durationMinutes = Duration.between(timeSlotRequest.getStartTime(), timeSlotRequest.getEndTime()).toMinutes();
        //check the venue is exist or not
        Venue venue = venueRepository.findById(timeSlotRequest.getVenueId())
                .orElseThrow(()-> new BadRequestException("The venue cannot be found by ID" + timeSlotRequest.getVenueId()));

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setDuration((int) durationMinutes);
        timeSlot.setStartTime(timeSlotRequest.getStartTime());
        timeSlot.setEndTime(timeSlotRequest.getEndTime());
        timeSlot.setPrice(timeSlotRequest.getPrice());
        timeSlot.setStatus(timeSlotRequest.isStatus());
        timeSlot.setVenue(venue);
        return timeSlotRepository.save(timeSlot);
    }    //Nên dùng try catch khi cố tạo hoặc thay đổi một đối tượng mới để handle lỗi

    // Cập nhật thông tin TimeSlot
    public TimeSlot updateTimeSlot(long timeSlotId, TimeSlotRequest timeSlotRequest) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(()-> new BadRequestException("Time slot cannot be found by ID: " + timeSlotId));

//        timeSlot.setDuration(timeSlotRequest.getDuration());
        timeSlot.setStartTime(timeSlotRequest.getStartTime());
        timeSlot.setEndTime(timeSlotRequest.getEndTime());
        timeSlot.setPrice(timeSlotRequest.getPrice());
        timeSlot.setStatus(timeSlot.isStatus());

        Venue venue = venueRepository.findById(timeSlotRequest.getVenueId())
                .orElseThrow(()-> new BadRequestException("The venue cannot be found by ID: "+ timeSlotRequest.getVenueId()));
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
                .orElseThrow(() -> new BadRequestException("TimeSlot cannot found by ID: " + timeSlotId));
    }

    // Read all TimeSlot
    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotRepository.findAll();
    }

    // Xoá một TimeSlot
    public void deleteTimeSlot(Long timeSlotId) {
        timeSlotRepository.deleteById(timeSlotId);
    }//Tự tạo hiển thị không có slot

}