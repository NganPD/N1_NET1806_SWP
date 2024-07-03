package online.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.CourtStatus;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "court_name", nullable = false)
    private String courtName;

    @Column(name = "court_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourtStatus status;

    @Column(nullable = true)
    private String services;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "venue_id", nullable = false)
    @JsonIgnore
    private Venue venue;

    @OneToMany(mappedBy = "court",cascade = CascadeType.ALL)
    private List<StaffVenue> staffCourts;

    @OneToMany(mappedBy = "court")
    private List<CourtTimeSlot> courtTimeSlots;

}
