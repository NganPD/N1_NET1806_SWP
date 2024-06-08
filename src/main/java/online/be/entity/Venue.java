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
    Long venueId;
    @Column(name = "venue_name", nullable = false)
    String name;

    @Column(name="address", nullable = false)
    String address;

    @Column(name="operating_hours", nullable = false)
    String operatingHours;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "paymentInfor", nullable = false)
    String paymentInfor;

    @Column(name = "number_of_courts")
    int numberOfCourts;

    @Column(name = "rating")
    Float rating;

    @Column(name = "image_URL")
    String imageURL;


}

