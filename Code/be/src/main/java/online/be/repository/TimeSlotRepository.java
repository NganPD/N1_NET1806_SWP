package online.be.repository;

import online.be.entity.TimeSlot;
import online.be.entity.Venue;
import online.be.enums.BookingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    // Tìm các TimeSlot theo độ dài
    List<TimeSlot> findByDuration(int duration);

    //Lấy danh sách tất cả các TimeSlot
    List<TimeSlot> findAll();

    // Tìm các TimeSlot theo thời gian bắt đầu nằm trong một khoảng thời gian
    List<TimeSlot> findByStartTimeBetween(LocalTime start, LocalTime end);

    List<TimeSlot> findByVenueVenueId(long venueId);

//    @Query("SELECT ts FROM TimeSlot ts " +
//            "WHERE ts.venue.id = :venueId " +
//            "AND ts.slotID NOT IN (" +
//            "   SELECT cs.timeSlot.slotID FROM CourtSchedule cs " +
//            "   WHERE cs.date = :date " +
//            "   AND cs.court.venue.id = :venueId " +
//            "   GROUP BY cs.timeSlot.slotID " +
//            "   HAVING COUNT(cs.court.id) = (SELECT COUNT(c.id) FROM Court c WHERE c.venue.id = :venueId)" +
//            ")")
//    List<TimeSlot> findAvailableTimeSlotsWithAtLeastOneCourtInVenue(
//            @Param("venueId") Long venueId,
//            @Param("date") LocalDate date
//    );
//
//    @Query("SELECT t FROM TimeSlot t" +
//            "WHERE t.venue.id = :venueId" +
//            "AND t.bookingType = :bookingType" +
//            "AND t.startTime = :startTime" +
//            "AND t.endTime = :endTime")
//    List<TimeSlot> findBYVenueIdAndBookingTypeAndStartTimeAndEndTime(
//            @Param("venueId") Long venueId,
//            @Param("bookingType")BookingType bookingType,
//            @Param("startTime") LocalTime startTime,
//            @Param("endTime") LocalTime endTime);
}
