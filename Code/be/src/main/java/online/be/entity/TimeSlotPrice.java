package online.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import online.be.enums.BookingType;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class TimeSlotPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingType bookingType;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "time_slot_id", nullable = false)
    private TimeSlot timeSlot;
}
