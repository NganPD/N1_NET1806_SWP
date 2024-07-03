package online.be.repository;

import online.be.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
        //tìm kiếm theo id của manager quản lý sân
        Venue findVenueByManagerId(long managerId);
        //tìm kiếm theo tên
        @Query("select v from Venue v " +
                "where v.description like %?1% " +
                "or v.name like %?1%")
        List<Venue> findVenueByKeywords(String keywords);

        //tìm kiêm sân theo available slot

//        @Query("SELECT v FROM Venue v WHERE v.startTime <= :startTime AND v.endTime >= :endTime")
//        List<Venue> findVenueWithAvailableSlots(LocalTime startTime, LocalTime endTime);

        List<Venue> findByAddress(String address);

        Venue findByName(String venueName);

        List<Venue> findByOperatingHours(@Param("operatingHours") LocalTime operatingHours);


}
