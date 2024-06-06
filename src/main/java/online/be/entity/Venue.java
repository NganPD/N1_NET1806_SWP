package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venueId;

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
}
