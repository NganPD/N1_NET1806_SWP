package online.be.repository;

import online.be.entity.Booking;
import online.be.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findBookingById(long id);
    List<Booking> findBookingByAccount_Id(long id);
    List<Booking> findByStatusAndAccount_Id(BookingStatus status, long accountId);
    List<Booking> findByBookingDateBeforeAndStatusAndAccount_Id(LocalDate bookingDate, BookingStatus status, long accountId);


}
