package online.be.repository;

import online.be.entity.Venue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;


import java.awt.print.Pageable;
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


        Venue findByName(String venueName);


        Venue findVenueById(long venueId);

}
