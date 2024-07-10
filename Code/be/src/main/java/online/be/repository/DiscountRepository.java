package online.be.repository;

import online.be.entity.Discount;
import online.be.enums.BookingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("SELECT tsp FROM TimeSlotPrice tsp " +
            "WHERE tsp.timeSlot.id = :timeSlotId " +
            "AND tsp.bookingType = :bookingType")
    Optional<Discount> findByTimeSlotAndBookingType(@Param("timeSlotId") long timeSlotId,
                                                    @Param("bookingType") BookingType bookingType);

    @Query("SELECT tsp FROM TimeSlotPrice tsp WHERE tsp.bookingType = :bookingType AND tsp.timeSlot.id = :timeSlotId")
    List<Discount> findByBookingTypeAndTimeSlotId(@Param("bookingType") BookingType bookingType, @Param("timeSlotId") Long timeSlotId);
    List<Discount> findByTimeSlotId(long timeSlotId);
    Discount findByBookingType(BookingType type);

}
