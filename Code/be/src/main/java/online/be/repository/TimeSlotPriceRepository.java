package online.be.repository;

import online.be.entity.TimeSlotPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotPriceRepository extends JpaRepository<TimeSlotPrice, Long> {
}