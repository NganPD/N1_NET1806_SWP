package online.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.BookingStatus;
import online.be.enums.SlotStatus;
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
    private SlotStatus status;

    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @ManyToOne
    @JoinColumn(name = "timeSlot_id", nullable = false)
    private TimeSlot timeSlot;

    @JsonIgnore
    @OneToOne(mappedBy = "courtTimeSlot")
    private BookingDetail bookingDetail;
}
