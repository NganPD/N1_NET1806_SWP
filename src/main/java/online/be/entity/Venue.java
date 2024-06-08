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
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long venueId;

    @Column(name = "venue_name", nullable = false)
    private String name;

    @Column(name="address", nullable = false)
    private String address;

    @Column(name="operating_hours", nullable = false)
    private String operatingHours;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "paymentInfor", nullable = false)
    private String paymentInfor;

    @Column(name = "number_of_courts")
    private int numberOfCourts;

    @Column(name = "rating")
    private float rating;

    @Column(name = "image_URL")
    private String imageURL;

    @OneToMany
    @JoinColumn(name = "courtId", nullable = false)
    private List<Court> courts;

    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "venue")
    private List<TimeSlot> slots;
}
