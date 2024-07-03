package online.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;
import online.be.enums.PaymentStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column( nullable = false)
    private double totalTimes;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = true)
    private double remainingTimes;//flexible booking
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingType bookingType;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime checkinDate;

    @Column(nullable = false)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "user_Id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "booking")
    private List<BookingDetail> bookingDetailList;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
