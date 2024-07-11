
package online.be.entity;

import jakarta.persistence.*;
import lombok.*;
import online.be.enums.TransactionEnum;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID transactionID;

    @Enumerated(EnumType.STRING)
    TransactionEnum transactionType;

    private float amount;

    private String description;

    private String transactionDate;

    private Long venueID;

    String accountNumber;

    String accountName;

    String bankName;

    String reasonWithdrawReject;

    @ManyToOne
    @JoinColumn(name = "from_id")
    Wallet from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    Wallet to;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    Booking booking;
}
