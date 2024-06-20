package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class CourtSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courtScheduleID;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "courtSchedule", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<TimeSlot> timeSlots;
}