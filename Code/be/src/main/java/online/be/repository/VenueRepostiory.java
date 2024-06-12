package online.be.repository;

import online.be.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepostiory extends JpaRepository<Venue, Long> {

}
