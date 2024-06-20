package online.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courtId;

    @Column(nullable = false)
    private String courtName;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable = false)
    private String amenities;

    @ManyToOne
    @JoinColumn(name = "venueId", nullable = false)
    @JsonIgnore
    private Venue venue;

}
