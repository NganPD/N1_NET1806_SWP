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
    private String courtName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String operatingHours;

    @Column
    private String description;
    @Column
    private String paymentInfo;
    @Column
    private int numberOfCourts;

    @ManyToOne
    @JoinColumn(name = "venueId", nullable = false)
    private Venue venue;

}
