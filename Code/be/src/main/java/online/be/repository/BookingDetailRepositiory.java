package online.be.repository;

import online.be.entity.BookingDetail;
import online.be.enums.BookingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailRepositiory extends JpaRepository<BookingDetail, Long> {

    List<BookingDetail> findByBookingId(Long bookingId);
    @Query("SELECT COUNT(bd) FROM BookingDetail bd " +
            "JOIN bd.courtTimeSlot cts " +
            "JOIN cts.court c " +
            "JOIN c.venue v " +
            "WHERE v.id = :venueId AND bd.booking.bookingType = :bookingType")
    long countByVenueIdAndBookingType(@Param("venueId") Long venueId, @Param("bookingType") String bookingType);

    @Query("SELECT SUM(bd.price) FROM BookingDetail bd " +
            "WHERE bd.booking.id = :bookingId")
    Double findTotalPriceByBookingId(long bookingId);

    @Query("SELECT bd.booking.bookingType, SUM(bd.price) FROM BookingDetail bd " +
            "JOIN bd.courtTimeSlot cts " +
            "JOIN cts.court c " +
            "WHERE c.id = :courtId AND MONTH(cts.checkInDate) = :month " +
            "AND YEAR(cts.checkInDate) = :year " +
            "GROUP BY bd.booking.bookingType")
    List<Object[]> findRevenueByCourtIdAndMonth(
            @Param("courtId") Long courtId,
            @Param("month") int month,
            @Param("year") int year);

    @Query("SELECT c.id, SUM(bd.price) FROM BookingDetail bd " +
            "JOIN bd.courtTimeSlot cts " +
            "JOIN cts.court c " +
            "JOIN c.venue v " +
            "WHERE v.id = :venueId AND MONTH(cts.checkInDate) = :month " +
            "AND YEAR(cts.checkInDate) = :year " +
            "GROUP BY c.id")
    List<Object[]> findRevenueByVenueIdAndMonth(
            @Param("venueId") Long venueId,
            @Param("month") int month,
            @Param("year") int year);
}
