package online.be.repository;

import online.be.entity.Court;
import online.be.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VenueRepostiory extends JpaRepository<Venue, Long> {
        Venue findVenueByManagerId(long managerId);
        List<Venue> findByName(String venueName);
        List<Venue> findByClosingHour(String closingHour);
        List<Venue> findByOpeningHours(String openingHour);

}
