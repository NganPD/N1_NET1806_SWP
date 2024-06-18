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

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "scheduleId", nullable = false)
    private CourtSchedule schedule



































            ;

}