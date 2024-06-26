package online.be.repository;

import online.be.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
//    List<Court> findCourtsByVenueId(long venueId);
//
//@Query("SELECT c FROM Court c WHERE c.courtType = :courtType")
//List<Court> findCourtByCourtType(@Param("courtType") String courtType);

}
