package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.PaymentStatus;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "refund_status")
    private boolean isRefund;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

}
