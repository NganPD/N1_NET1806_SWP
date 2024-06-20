package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Getter
@Setter
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingDetailId;

    @Column
    private Double totalPrice;

    @Column
    private Double totalHours;

//    @OneToOne
//    @JoinColumn(name = "booking_id", nullable = false)
//    private Booking booking;
//
//    @ManyToOne
//    @JoinColumn(name = "timeSlot_id", nullable = false)
//    private TimeSlot timeSlot;


}
