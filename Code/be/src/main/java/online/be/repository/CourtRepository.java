package online.be.repository;

import online.be.entity.Court;
import online.be.entity.Venue;
import online.be.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
    List<Court> findByVenue(Venue venue);
    int countByVenueId(long venueId);

    @Query("SELECT DISTINCT c FROM Court c " +
            "JOIN c.courtTimeSlots cts " +
            "JOIN c.venue v " +
            "WHERE v.id = :venueId " +
            "AND cts.timeSlot.id IN :timeSlotIds " +
            "AND cts.status = :status OR cts.status IS NULL " +
            "AND cts.checkInDate = :checkInDate")
    List<Court> findAvailableCourtsByVenueAndTimeSlotsAndDate(
            @Param("venueId") long venueId,
            @Param("timeSlotIds") List<Long> timeSlotIds,
            @Param("status") SlotStatus status,
            @Param("checkInDate") LocalDate checkInDate);

    @Query(value = "SELECT DISTINCT a.court_id " +
            "FROM court_time_slot a " +
            "WHERE a.time_slot_id IN :timeSlotIds AND a.check_in_date = :checkInDate AND a.status = 'BOOKED'",
            nativeQuery = true)
    List<Long> findCourtIdsByTimeSlotsAndDate(@Param("timeSlotIds") List<Long> timeSlotIds, @Param("checkInDate") LocalDate checkInDate);

    @Query("SELECT c FROM Court c " +
            "JOIN c.venue v " +
            "WHERE v.id = :venueId")
    List<Court> findAllCourtsByVenue(long venueId);
}
