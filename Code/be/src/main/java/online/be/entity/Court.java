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
    private long id;

    @Column(name = "court_name", nullable = false)
    private String courtName;

    @Column(name = "court_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourtStatus status;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @JsonIgnore
    @OneToMany(mappedBy = "court")
    private List<CourtTimeSlot> courtTimeSlots;

}