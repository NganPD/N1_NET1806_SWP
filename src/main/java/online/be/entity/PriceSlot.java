package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class PriceSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long priceSlotId;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "venueId", nullable = false)
    private Venue venue;
}

