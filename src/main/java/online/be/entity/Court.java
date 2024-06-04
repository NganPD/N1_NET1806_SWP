package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courtId;

    String courtName;

    String location;

    String operatingHours;

    String description;

    String paymentInfo;

    int quantity;

    @OneToMany(mappedBy = "Account")
    long id;
}
