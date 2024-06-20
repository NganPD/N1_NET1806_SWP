package online.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private Double price;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "scheduleId", nullable = false)
    private CourtSchedule schedule



































            ;

}