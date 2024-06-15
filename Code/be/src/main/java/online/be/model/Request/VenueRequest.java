package online.be.model.Request;

import lombok.Data;
import online.be.entity.Account;
import online.be.entity.Court;

import java.util.List;

@Data
public class VenueRequest {
    private String venueName;
    private String address;
    private String operatingHours;
    private String closingHours;
    private String paymentInfor;
    private String description;
    private List<Long> courtId;
    private Long managerId;
}
