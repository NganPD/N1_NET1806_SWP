package online.be.model.Request;

import lombok.Data;
import online.be.enums.VenueStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
public class CreateVenueRequest {
    private String venueName;
    private String address;
    private String contactInfor;//sdt hoặc email
    private VenueStatus venueStatus;
    private String openingHours;
    private String closingHours;
    private String description;
    private String services;
    private Long managerId;//reference từ account
}
