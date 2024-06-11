package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long slotID;

    @Column(nullable = false)
    private int duration; // in minutes

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "courtScheduleID", nullable = false)
    private CourtSchedule courtSchedule;

    @ManyToOne
    @JoinColumn(name = "venueId", nullable = false)
    private Venue venue;

    @OneToMany(mappedBy = "timeSlot",orphanRemoval = true)
    private List<TimeSlot> timeSlots;
}