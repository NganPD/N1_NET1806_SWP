package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.VenueStatus;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long venueId;

    @Column(name = "venue_name", nullable = false)
    private String name;

    @Column(name="address", nullable = false)
    private String address;

    @Column(name ="venue_status",nullable = false)
    private VenueStatus venueStatus;

    @Column(name="operating_hours", nullable = false)
    private String operatingHours;

    @Column(name="closing_hours", nullable = false)
    private String closingHours;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "venue")
    private List<Court> courts;

    @OneToOne
    @JoinColumn(name="courtScheduleId", nullable = false)
    private CourtSchedule courtSchedule;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Account manager;

    @OneToOne
    @JoinColumn(name = "payment_account_id", nullable = false)
    private PaymentAccount paymentAccount;
}
