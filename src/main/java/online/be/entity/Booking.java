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
    private long bookingId;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "total_hours", nullable = false)
    private double totalHours;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "bookingType", nullable = false)
    private BookingType bookingType;

    @ManyToOne
    @JoinColumn(name = "user_Id", nullable = false)
    private Account account;

    @OneToOne
    @JoinColumn(name = "payment_Id")
    private Payment payment;
}