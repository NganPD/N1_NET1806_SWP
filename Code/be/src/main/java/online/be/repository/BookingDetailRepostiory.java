package online.be.repository;

import online.be.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDetailRepostiory extends JpaRepository<BookingDetail, Long> {
}
