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
    private long courtId;

    @Column( nullable = false)
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
    private Venue venue;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourtSchedule> courtSchedules;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookingDetail> bookingDetails;

    @OneToMany(mappedBy = "court",cascade = CascadeType.ALL)
    private List<StaffCourt> staffCourts;

}
