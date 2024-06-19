package online.be.repository;

import online.be.entity.Court;
import online.be.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VenueRepostiory extends JpaRepository<Venue, Long> {
        //tìm kiếm theo id của manager quản lý sân
        Venue findVenueByManagerId(long managerId);
        //tìm kiếm theo tên
        List<Venue> findByName(String venueName);
        //tìm kiếm theo giờ mở cửa
        List<Venue> findByOpeningHours(String openingHour);
        //tìm kiêm sân theo available slot
        @Query("select v from Venue v " +
                "join v.courts c " +
                "join c.timeslots ts " +
                "where ts.startTime >= :startDateTime " +
                "and ts.endTime <= :endDateTime " +
                "and ts.available = true")
        List<Venue> findVenueWithAvailableSlots(
                @Param("startDateTime")LocalDateTime startDateTime,
                @Param("endDateTime")LocalDateTime endDateTime
                );

}
