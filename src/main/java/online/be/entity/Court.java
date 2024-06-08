package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courtId;

    @Column(nullable = false)
    private String courtName;

    @Column(nullable = false)
    private String status;

    @Column
    private String amenities;

}
