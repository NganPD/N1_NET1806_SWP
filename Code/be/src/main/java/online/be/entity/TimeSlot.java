package online.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long slotID;

    @Column(nullable = false)
    private int duration; // in minutes

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @OneToMany(mappedBy = "timeSlot")
    private List<CourtSchedule> courtSchedules;
}
