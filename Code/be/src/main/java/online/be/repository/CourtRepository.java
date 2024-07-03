package online.be.repository;

import online.be.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    @Query("SELECT c FROM Court c" +
            "WHERE c.venue.venueId = :venueId" +
            "AND c.courtId NOT IN " +
            "(SELECT bd.court.courtId FROM BookingDetail bd " +
            "JOIN TimeSlot ts ON bd.timeSlot.slotID " +
            "WHERE ts.slotID = :timeslotId" +
            "AND bd.status = 'CONFIRMED')")
    List<Court> findAvailableCourtsForTimeSlot(@Param("venueId") Long venueId,
                                      @Param("timeSlotId") Long timeSlotId);
}
