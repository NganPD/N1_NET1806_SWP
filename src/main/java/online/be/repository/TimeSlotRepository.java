package online.be.repository;

import online.be.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    // Tìm các TimeSlot theo độ dài
    List<TimeSlot> findByDuration(int duration);

    // Tìm các TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    List<TimeSlot> findByStartTimeBetween(LocalTime start, LocalTime end);
}
