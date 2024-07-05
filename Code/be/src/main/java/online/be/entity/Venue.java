package online.be.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.VenueStatus;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VenueStatus venueStatus;

    private String services;

    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime operatingHours;

    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime closingHours;

    @Column(nullable = false)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Court> courts;

    @JsonIgnore
    @OneToMany(mappedBy = "venue")
    private List<TimeSlot> timeSlots;

    @JsonIgnore
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "managerId")
    private Account manager;

    @JsonIgnore
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<PaymentAccount> paymentInforList;
}
