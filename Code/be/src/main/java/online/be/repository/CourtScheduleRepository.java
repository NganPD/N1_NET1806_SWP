package online.be.repository;

import online.be.entity.CourtSchedule;
import online.be.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourtScheduleRepository extends JpaRepository<CourtSchedule, Long> {

    @Query("SELECT cs FROM CourtSchedule cs WHERE cs.timeSlot = :timeSlot")
    List<CourtSchedule> findByTimeSlot(@Param("timeSlot") TimeSlot timeSlot);
}
