package online.be.repository;

import online.be.entity.BookingDetail;
import online.be.entity.CourtTimeSlot;
import online.be.enums.BookingType;
import online.be.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface CourtTimeSlotRepository extends JpaRepository<CourtTimeSlot, Long> {

//    @Query("SELECT cts FROM CourtTimeSlot cts" +
//            "WHERE cts.timeSlot.id = :timeSlotId" +
//            "AND cts.status = :status")
//    List<CourtTimeSlot> findAvailableCourtTimeSlots(@Param("timeSlotId") long timeSlotId,
//                                                    @Param("status")SlotStatus status);

//    @Query("SELECT cts " +
//            "FROM CourtTimeSlot cts " +
//            "JOIN cts.timeSlot ts " +
//            "JOIN ts.timeSlotPrices tsp " +
//            "WHERE cts.court.id = :courtId " +
//            "AND ts.id = :timeSlotId " +
//            "AND tsp.bookingType = :bookingType " +
//            "AND cts.status = 'AVAILABLE'")  // Assuming you want only available slots
//    CourtTimeSlot findAvailableCourtTimeSlotsByCourtIdAndTimeSlotIdAndBookingType(
//            @Param("courtId") long courtId,
//            @Param("timeSlotId") long timeSlotId,
//            @Param("bookingType") BookingType bookingType);
//
//    @Query("SELECT ts.duration " +
//            "FROM CourtTimeSlot cts " +
//            "JOIN cts.timeSlot ts " +
//            "WHERE cts.id = :courtTimeSlotId")
//    long findDurationByCourTimeSlotId(@Param("courtTimeSlotId") long courtTimeSlotId);

    CourtTimeSlot findByBookingDetail(@Param("BookingDetail") BookingDetail bookingDetail);

//    @Query("SELECT cts " +
//            "FROM CourtTimeSlot cts " +
//            "JOIN cts.timeSlot ts " +
//            "WHERE cts.date = :date " +
//            "AND ts.startTime = :startTime " +
//            "AND ts.endTime = :endTime " +
//            "AND cts.status = 'AVAILABLE'")
//    Optional<CourtTimeSlot> findByDayAndTime(@Param("date") LocalDate date,
//                                             @Param("startTime") LocalTime startTime,
//                                             @Param("endTime") LocalTime endTime);

//    @Query("SELECT cts FROM CourtTimeSlot cts" +
//            "WHERE cts.timeSlot.id = :timeSlotId" +
//            "AND cts.status = :status")
//    List<CourtTimeSlot> findAvailableCourtTimeSlots(@Param("timeSlotId") long timeSlotId,
//                                                    @Param("status")SlotStatus status);

//    @Query("SELECT cts " +
//            "FROM CourtTimeSlot cts " +
//            "JOIN cts.timeSlot ts " +
//            "JOIN ts.timeSlotPrices tsp " +
//            "WHERE cts.court.id = :courtId " +
//            "AND ts.id = :timeSlotId " +
//            "AND tsp.bookingType = :bookingType " +
//            "AND cts.status = 'AVAILABLE'")  // Assuming you want only available slots
//    Optional<CourtTimeSlot> findAvailableCourtTimeSlotsByCourtIdAndTimeSlotIdAndBookingType(
//            @Param("courtId") Long courtId,
//            @Param("timeSlotId") Long timeSlotId,
//            @Param("bookingType") BookingType bookingType);

    CourtTimeSlot findById(long id);

    @Query("SELECT cts FROM CourtTimeSlot cts JOIN cts.court court WHERE cts.status = :status AND court.venue.id = :venueId")
    List<CourtTimeSlot> findByStatusAndVenueId(@Param("status") SlotStatus status, @Param("venueId") long venueId);

    @Query("SELECT cts FROM CourtTimeSlot cts WHERE cts.court.id = :courtId AND cts.timeSlot.id IN :timeSlotIds AND cts.checkInDate = :date")
    List<CourtTimeSlot> findByCourtIdAndTimeSlotIdsAndDate(@Param("courtId") Long courtId, @Param("timeSlotIds") List<Long> timeSlotIds, @Param("date") LocalDate date);

    @Query("SELECT cts FROM CourtTimeSlot cts JOIN cts.court court " +
            "WHERE court.venue.id = :venueId " +
            "AND cts.checkInDate = :checkInDate " +
            "AND cts.status IN ('BOOKED', 'CHECKED')")
    List<CourtTimeSlot> findByVenueIdAndDateAndStatus(@Param("venueId") long venueId,
                                                      @Param("checkInDate") LocalDate checkInDate);

    @Query("SELECT COUNT(cts) > 0 " +
            "FROM CourtTimeSlot cts " +
            "JOIN cts.timeSlot ts " +
            "JOIN cts.court c " +
            "WHERE ts.id = :timeSlotId " +
            "AND c.id = :courtId " +
            "AND cts.status = 'AVAILABLE' " +
            "AND cts.checkInDate = :checkInDate")
    boolean existsByTimeSlotAndCourt(@Param("timeSlotId") Long timeSlotId,
                                     @Param("courtId") Long courtId,
                                     @Param("checkInDate") LocalDate checkInDate);
}


