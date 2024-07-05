package online.be.repository;

import online.be.entity.CourtTimeSlot;
import online.be.enums.BookingType;
import online.be.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourtTimeSlotRepository extends JpaRepository<CourtTimeSlot, Long> {

//    @Query("SELECT cts FROM CourtTimeSlot cts" +
//            "WHERE cts.timeSlot.id = :timeSlotId" +
//            "AND cts.status = :status")
//    List<CourtTimeSlot> findAvailableCourtTimeSlots(@Param("timeSlotId") long timeSlotId,
//                                                    @Param("status")SlotStatus status);

    @Query("SELECT cts " +
            "FROM CourtTimeSlot cts " +
            "JOIN cts.timeSlot ts " +
            "JOIN ts.timeSlotPrices tsp " +
            "WHERE cts.court.id = :courtId " +
            "AND ts.id = :timeSlotId " +
            "AND tsp.bookingType = :bookingType " +
            "AND cts.status = 'AVAILABLE'")  // Assuming you want only available slots
    CourtTimeSlot findAvailableCourtTimeSlotsByCourtIdAndTimeSlotIdAndBookingType(
            @Param("courtId") long courtId,
            @Param("timeSlotId") long timeSlotId,
            @Param("bookingType") BookingType bookingType);

    @Query("SELECT ts.duration " +
            "FROM CourtTimeSlot cts " +
            "JOIN cts.timeSlot ts " +
            "WHERE cts.id = :courtTimeSlotId")
    long findDurationByCourTimeSlotId(@Param("courtTimeSlotId") long courtTimeSlotId);
}
