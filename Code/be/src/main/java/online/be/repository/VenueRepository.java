package online.be.repository;

import online.be.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
        //tìm kiếm theo id của manager quản lý sân
        Venue findVenueByManagerId(long managerId);
        //tìm kiếm theo tên
        @Query("select v from Venue v " +
                "where v.description like %?1% " +
                "or v.name like %?1%")
        List<Venue> searchVenue(String keywords);
//        //tìm kiếm theo giờ mở cửa

//        List<Venue> findByOpeningHours(String openingHour);
        //tìm kiêm sân theo available slot

//        @Query("SELECT DISTINCT v FROM Venue v " +
//                "JOIN v.courts c " +
//                "JOIN c.timeslots ts " +
//                "WHERE ts.startTime >= :startDateTime " +
//                "AND ts.endTime <= :endDateTime " +
//                "AND ts.status = true")
//        List<Venue> findVenueWithAvailableSlots(@Param("startDateTime") LocalDateTime startDateTime,
//                                                @Param("endDateTime") LocalDateTime endDateTime);

//        //tim kiem theo dia chi
//        @Query("select v from Venue v " +
//                "where v.address like %?1% ")
//        List<Venue> findVenueWithLocation(String address);

}
