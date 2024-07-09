package online.be.repository;

import online.be.entity.TimeSlot;
import online.be.entity.Venue;
import online.be.enums.BookingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    //Lấy danh sách tất cả các TimeSlot
    List<TimeSlot> findAll();

    // Tìm các TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    List<TimeSlot> findByStartTimeBetween(LocalTime start, LocalTime end);

    List<TimeSlot> findByVenueId(long venueId);

    // Custom query to get count of time slots for a specific courtId and checkInDate
    @Query(value = "SELECT a.time_slot_id, COUNT(a.time_slot_id) " +
            "FROM court_time_slot a " +
            "WHERE a.court_id = :courtId AND a.check_in_date = :date " +
            "GROUP BY a.time_slot_id", nativeQuery = true)
    List<Object[]> countTimeSlotsByCourtIdAndDate(@Param("courtId") long courtId, @Param("date") LocalDate date);
    //Get timeslot by venue
    List<TimeSlot>getTimeSlotByVenue_Id(long venueId);
}
