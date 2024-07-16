package online.be.repository;

import online.be.entity.Pricing;
import online.be.enums.BookingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    List<Pricing> findByVenue_IdAndBookingType(long venueId, BookingType bookingType);
}
