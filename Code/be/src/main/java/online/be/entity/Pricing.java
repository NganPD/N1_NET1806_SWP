package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.BookingType;

@Entity
@Getter
@Setter
@ToString
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private BookingType bookingType;

    private double pricePerHour;

    @ManyToOne
    @JoinColumn(name = "timeslot_id")
    private TimeSlot timeSlot;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;
}
