package online.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import online.be.enums.BookingType;

@Entity
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double discount;

    private String description;

    @JsonIgnore
    @OneToOne(mappedBy = "discount", cascade = CascadeType.ALL)
    private Booking booking;
}