package online.be.repository;

import online.be.entity.Court;
import online.be.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
    List<Court> findByVenue(Venue venue);
    int countByVenueId(long venueId);
}
