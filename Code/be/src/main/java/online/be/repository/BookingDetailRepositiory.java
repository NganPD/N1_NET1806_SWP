package online.be.repository;

import online.be.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailRepositiory extends JpaRepository<BookingDetail, Long> {

    List<BookingDetail> findByBookingId(Long bookingId);

    @Query("SELECT c.courtName, " +
            "SUM(CASE WHEN bd.booking.bookingType = 'FIXED' THEN bd.price ELSE 0 END) AS fixed, " +
            "SUM(CASE WHEN bd.booking.bookingType = 'DAILY' THEN bd.price ELSE 0 END) AS daily, " +
            "SUM(CASE WHEN bd.booking.bookingType = 'FLEXIBLE' THEN bd.price ELSE 0 END) AS flexible " +
            "FROM BookingDetail bd " +
            "JOIN bd.courtTimeSlot cts " +
            "JOIN cts.court c " +
            "JOIN c.venue v " +
            "WHERE v.id = :venueId " +
            "AND MONTH(cts.checkInDate) = :month " +
            "AND YEAR(cts.checkInDate) = :year " +
            "AND bd.booking.status = 'BOOKED' " +
            "GROUP BY c.courtName")
    List<Object[]> findRevenueByCourtAndBookingType(
            @Param("venueId") Long venueId,
            @Param("month") int month,
            @Param("year") int year
    );
}
