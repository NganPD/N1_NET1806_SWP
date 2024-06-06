package online.be.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.BookingType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long bookingId;

    @Column(name = "booking_date", nullable = false)
    LocalDate bookingDate;

    @Column(name = "total_hours", nullable = false)
    double totalHours;

    @Column(name = "total_price", nullable = false)
    BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "bookingType", nullable = false)
    BookingType bookingType;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    Account account;

}
