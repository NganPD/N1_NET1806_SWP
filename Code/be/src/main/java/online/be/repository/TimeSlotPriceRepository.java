package online.be.repository;

import online.be.entity.TimeSlotPrice;
import online.be.enums.BookingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimeSlotPriceRepository extends JpaRepository<TimeSlotPrice, Long> {

    @Query("SELECT tsp FROM TimeSlotPrice tsp " +
            "WHERE tsp.timeSlot.id = :timeSlotId " +
            "AND tsp.bookingType = :bookingType")
    TimeSlotPrice findByTimeSlotAndBookingType(@Param("timeSlotId") long timeSlotId,
                                               @Param("bookingType") BookingType bookingType);
}