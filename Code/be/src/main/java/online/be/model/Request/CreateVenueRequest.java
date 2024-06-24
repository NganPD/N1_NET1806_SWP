package online.be.model.Request;

import lombok.Data;
import online.be.enums.VenueStatus;

@Data
public class CreateVenueRequest {
    private String venueName;
    private String address;
    private VenueStatus venueStatus;
    private String operatingHours;
    private String closingHours;
    private String description;
    private Long managerId;//reference từ account
}
