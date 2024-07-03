package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "court_id")
    private Court court;

    @OneToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

}
