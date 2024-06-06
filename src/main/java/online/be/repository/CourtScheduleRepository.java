package online.be.repository;

import online.be.entity.CourtSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourtScheduleRepository extends JpaRepository<CourtSchedule, Long> {
    // Tìm các CourtSchedule theo trạng thái
    List<CourtSchedule> findByStatus(String status);

    // Tìm các CourtSchedule theo ngày
    List<CourtSchedule> findByDate(LocalDate date);
}
