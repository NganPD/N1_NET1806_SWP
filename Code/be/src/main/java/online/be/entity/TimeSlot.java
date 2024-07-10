package online.be.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime endTime;

    private long duration;
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @JsonIgnore
    @OneToMany(mappedBy = "timeSlot", fetch = FetchType.LAZY)
    private List<Discount> discounts;

    @JsonIgnore
    @OneToMany(mappedBy = "timeSlot")
    private List<CourtTimeSlot> courtTimeSlots;
}
