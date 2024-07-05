package online.be.repository;

import online.be.entity.CourtTimeSlot;
import online.be.entity.TimeSlotPrice;
import online.be.enums.BookingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotPriceRepository extends JpaRepository<TimeSlotPrice, Long> {

    @Query("SELECT tsp FROM TimeSlotPrice tsp " +
            "WHERE tsp.timeSlot.id = :timeSlotId " +
            "AND tsp.bookingType = :bookingType")
    Optional<TimeSlotPrice> findByTimeSlotAndBookingType(@Param("timeSlotId") long timeSlotId,
                                                         @Param("bookingType") BookingType bookingType);

    @Query("SELECT tsp FROM TimeSlotPrice tsp WHERE tsp.bookingType = :bookingType AND tsp.timeSlot.id = :timeSlotId")
    List<TimeSlotPrice> findByBookingTypeAndTimeSlotId(@Param("bookingType") BookingType bookingType, @Param("timeSlotId") Long timeSlotId);
}