package online.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PaymentAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentAccountID;

    @Column(name = "payment_name", nullable = false)
    private String paymentName;

    @Column(name = "account_holder_name", nullable = false)
    private String accountHolderName;

    @OneToOne
    @JoinColumn(name = "venue_id",nullable = false)
    private Venue venue;

}
