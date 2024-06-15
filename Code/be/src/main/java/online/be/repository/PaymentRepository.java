package online.be.repository;

import online.be.entity.Payment;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface  PaymentRepository extends JpaRepository<Payment, Long> {

}
