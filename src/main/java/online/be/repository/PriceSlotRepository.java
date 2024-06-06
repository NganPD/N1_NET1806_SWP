package online.be.repository;

import online.be.entity.PriceSlot;
import online.be.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceSlotRepository extends JpaRepository<PriceSlot, Long> {
    // Tìm các PriceSlot theo giá
    List<PriceSlot> findByPrice(double price);

    // Tìm các PriceSlot theo Venue
    List<PriceSlot> findByVenue(Venue venue);
}
