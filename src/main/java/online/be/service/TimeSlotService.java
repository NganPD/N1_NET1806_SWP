package online.be.service;

import online.be.entity.CourtSchedule;
import online.be.entity.TimeSlot;
import online.be.model.Request.TimeSlotRequest;
import online.be.repository.CourtScheduleRepository;
import online.be.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    private final CourtScheduleRepository courtScheduleRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, CourtScheduleRepository courtScheduleRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.courtScheduleRepository = courtScheduleRepository;
    }

    // Lưu một TimeSlot mới hoặc cập nhật một TimeSlot đã tồn tại
    public TimeSlot createTimeSlot(TimeSlotRequest timeSlotRequest) {
        TimeSlot timeSlot = new TimeSlot();
        CourtSchedule courtSchedule = courtScheduleRepository.findById(timeSlotRequest.getCourtScheduleId()).get();
        if (courtSchedule == null) {
            throw new RuntimeException("CourtScheduleId is not existed: " + timeSlotRequest.getCourtScheduleId());
        }
        timeSlot.setDuration(timeSlotRequest.getDuration());
        timeSlot.setStartTime(timeSlotRequest.getStartTime());
        timeSlot.setEndTime(timeSlotRequest.getEndTime());
        timeSlot.setCourtSchedule(courtSchedule);
        return timeSlotRepository.save(timeSlot);
    }

    // Cập nhật thông tin TimeSlot
    public TimeSlot updateTimeSlot(long timeSlotId, TimeSlotRequest timeSlotRequest) {
        Optional<TimeSlot> timeSlotOptional = timeSlotRepository.findById(timeSlotId);
        if (timeSlotOptional.isEmpty()) {
            throw new RuntimeException("TimeSlot not found with ID: " + timeSlotId);
        }
        TimeSlot timeSlot = new TimeSlot();
        CourtSchedule courtSchedule = courtScheduleRepository.findById(timeSlotRequest.getCourtScheduleId()).get();
        if (courtSchedule == null) {
            throw new RuntimeException("CourtScheduleId is not existed: " + timeSlotRequest.getCourtScheduleId());
        }
        timeSlot.setDuration(timeSlotRequest.getDuration());
        timeSlot.setStartTime(timeSlotRequest.getStartTime());
        timeSlot.setEndTime(timeSlotRequest.getEndTime());
        timeSlot.setCourtSchedule(courtSchedule);
        return timeSlotRepository.save(timeSlot);
    }

    // Tìm các TimeSlot theo độ dài
    public List<TimeSlot> findByDuration(int duration) {
        return timeSlotRepository.findByDuration(duration);
    }

    // Tìm các TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    public List<TimeSlot> findByStartTimeBetween(LocalTime start, LocalTime end) {
        return timeSlotRepository.findByStartTimeBetween(start, end);
    }

    // Hiển thị TimeSlot dựa trên id
    public TimeSlot getTimeSlotById(long timeSlotId) {
        Optional<TimeSlot> timeSlotOptional = timeSlotRepository.findById(timeSlotId);
        if (timeSlotOptional.isPresent()) {
            return timeSlotOptional.get();
        } else {
            throw new RuntimeException("TimeSlot not found with ID: " + timeSlotId);
        }
    }

    // Hiển thị toàn bộ TimeSlot
    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotRepository.findAll();
    }

    // Xoá một TimeSlot
    public void deleteTimeSlot(Long timeSlotId) {
        timeSlotRepository.deleteById(timeSlotId);
    }
}