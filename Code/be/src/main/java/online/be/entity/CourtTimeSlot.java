package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.BookingStatus;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Getter
@Setter
@ToString
public class CourtTimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @ManyToOne
    @JoinColumn(name = "timeSlot_id", nullable = false)
    private TimeSlot timeSlot;

    @OneToOne
    @JoinColumn(name = "bookingDetail_id", nullable = false)
    private BookingDetail bookingDetail;
}
