package online.be.repository;

import online.be.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueRepostiory extends JpaRepository<Venue, Long> {

}
