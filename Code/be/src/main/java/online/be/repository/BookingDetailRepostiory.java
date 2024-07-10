package online.be.repository;

import online.be.entity.Booking;
import online.be.entity.BookingDetail;
import online.be.entity.CourtTimeSlot;
import online.be.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingDetailRepostiory extends JpaRepository<BookingDetail, Long> {


    List<BookingDetail> findByBookingId(long bookingId);

    @Query("SELECT SUM(bd.price) FROM BookingDetail bd " +
            "WHERE bd.booking.id = :bookingId")
    Double findTotalPriceByBookingId(long bookingId);

}
