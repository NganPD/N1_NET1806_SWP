package online.be.service;

import online.be.entity.TimeSlot;
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

    // Lấy tất cả các TimeSlot
    public List<TimeSlot> findAll() {
        return timeSlotRepository.findAll();
    }

    // Tìm TimeSlot theo ID
    public Optional<TimeSlot> findById(long id) {
        return timeSlotRepository.findById(id);
    }

    // Lưu một TimeSlot mới hoặc cập nhật một TimeSlot đã tồn tại
    public TimeSlot save(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }

    // Xóa một TimeSlot theo ID
    public void deleteById(long id) {
        timeSlotRepository.deleteById(id);
    }

    // Tìm các TimeSlot theo độ dài
    public List<TimeSlot> findByDuration(int duration) {
        return timeSlotRepository.findByDuration(duration);
    }

    // Tìm các TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    public List<TimeSlot> findByStartTimeBetween(LocalTime start, LocalTime end) {
        return timeSlotRepository.findByStartTimeBetween(start, end);
    }
}
