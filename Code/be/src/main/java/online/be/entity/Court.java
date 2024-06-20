package online.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.CourtStatus;
import online.be.enums.CourtType;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

@Entity
@Getter
@Setter
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courtId;

    @Column(name = "court_name", nullable = false)
    private String courtName;

    @Column(name = "court_status", nullable = false)
    private CourtStatus status;

    @Column(name = "amenities")
    private String amenities;

    @Column(name = "description")
    private String description;

    @Column(name = "court_type", nullable = false)
    private CourtType courtType;

    @ManyToOne
    @JoinColumn(name = "venueId", nullable = false)
    @JsonIgnore
    private Venue venue;

    @OneToMany(mappedBy = "account_id")
    List<Account> staffs;

//    @OneToOne
//    @JoinColumn(name = "scheduleId", nullable = false)
//    private CourtSchedule schedule;

}
