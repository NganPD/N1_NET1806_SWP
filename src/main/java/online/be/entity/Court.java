package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courtId;

    @Column(nullable = false)
    String courtName;

    @Column(nullable = false)
    String location;

    @Column(nullable = false)
    String operatingHours;

    @Column
    String description;
    @Column
    String paymentInfo;
    @Column
    int numberOfCourts;

    @ManyToOne
    @JoinColumn(name = "venueId", nullable = false)
    Venue venue;

}
