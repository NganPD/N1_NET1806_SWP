package online.be.repository;

import online.be.entity.CourtSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtScheduleRepository extends JpaRepository<CourtSchedule, Long> {
}
