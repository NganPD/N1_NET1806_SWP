package online.be.repository;

import online.be.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    //Tìm TimeSlot theo Id
    Optional<TimeSlot> findById(Long id);

    // Tìm các TimeSlot theo độ dài
    List<TimeSlot> findByDuration(int duration);

    //Lấy danh sách tất cả các TimeSlot
    List<TimeSlot> findAll();

    // Tìm các TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    List<TimeSlot> findByStartTimeBetween(LocalTime start, LocalTime end);
}
