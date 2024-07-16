
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

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;
    private long duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @JsonIgnore
    @OneToMany(mappedBy = "timeSlot", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CourtTimeSlot> courtTimeSlots;


    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL)
    private List<Pricing> pricingList;

}
