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
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "court_id")
    private long courtId;

    @Column(name = "court_name", nullable = false)
    private String courtName;

    @Column(name = "court_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourtStatus status;

    @Column(name = "amenities")
    private String amenities;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "venue_id", nullable = false)
    @JsonIgnore
    private Venue venue;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourtSchedule> courtSchedules;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookingDetail> bookingDetails;

    @OneToMany(mappedBy = "court",cascade = CascadeType.ALL)
    private List<StaffCourt> staffCourts;

}
