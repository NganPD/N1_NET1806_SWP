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
    @Query("SELECT bd FROM BookingDetail bd " +
            "WHERE bd.courtTimeSlot.court.id = :courtId " +
            "AND bd.status = :status")
    List<BookingDetail> findByCourtAndStatus(long courtId, BookingStatus status);

    List<BookingDetail> findByCourtTimeSlot_TimeSlot_TimeSlotPricesId(long id);

    List<BookingDetail> findByBookingId(long bookingId);

    @Query("SELECT SUM(bd.price) FROM BookingDetail bd " +
            "WHERE bd.booking.id = :bookingId")
    Double findTotalPriceByBookingId(long bookingId);

    @Query("SELECT bd FROM BookingDetail bd WHERE bd.booking = :booking AND bd.courtTimeSlot = :courtTimeSlot AND bd.date = :date")
    BookingDetail findByBookingAndCourtTimeSlotAndDate(
            @Param("booking") Booking booking,
            @Param("courtTimeSlot") CourtTimeSlot courtTimeSlot,
            @Param("date") LocalDate date);
}
