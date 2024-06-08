package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column
    private double Amount;

    @Column(nullable = false)
    private boolean paymentStatus;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @OneToOne(mappedBy = "payment")
    private Booking booking;

}
