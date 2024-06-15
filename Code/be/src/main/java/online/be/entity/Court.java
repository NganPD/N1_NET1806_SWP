package online.be.entity;

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
@ToString
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courtId;

    @Column(nullable = false)
    private String courtName;

    @Column(nullable = false)
    private CourtStatus status;

    @Column
    private String amenities;

    @Column
    private String description;

    @Column(nullable = false)
    private CourtType courtType;

    @ManyToOne
    @JoinColumn(name = "venueId", nullable = false)
    private Venue venue;

    @OneToMany(mappedBy = "account_id")
    List<Account> staffs;

//    @OneToOne
//    @JoinColumn(name = "scheduleId", nullable = false)
//    private CourtSchedule schedule;

}
