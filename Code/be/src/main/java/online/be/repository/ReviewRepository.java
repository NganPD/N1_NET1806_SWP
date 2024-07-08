package online.be.repository;

import jdk.jfr.Registered;
import online.be.entity.Review;
import online.be.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByAccount_IdAndVenue_id(long accountId, long venueId);

    @Query("SELECT v FROM Venue v LEFT JOIN Review r ON v.id = r.venue.id " +
            "GROUP BY v.id ORDER BY AVG(r.rating) DESC")
    List<Venue> findTopRatedVenues();

    @Query("SELECT v FROM Venue v LEFT JOIN Review r ON v.id = r.venue.id " +
            "WHERE r.rating >= :minRating " +
            "GROUP BY v.id ORDER BY COUNT(r.id) DESC")
    List<Venue> findVenueWithHighRatings(int minRating);


    @Query("SELECT v FROM Venue v LEFT JOIN Booking b ON v.id = b.venue.id " +
            "GROUP BY v.id ORDER BY COUNT(b.id) DESC")
    List<Venue> findMostBookedVenues();
}
