package online.be.model.Response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.be.entity.*;
import online.be.enums.TransactionEnum;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private UUID transactionID;

    @Enumerated(EnumType.STRING)
    TransactionEnum transactionType;

    private float amount;

    private String description;

    private String transactionDate;

    private Venue venue;
    private Booking booking;

    Wallet from;

    Wallet to;

    Account userFrom;

    Account userTo;

}
