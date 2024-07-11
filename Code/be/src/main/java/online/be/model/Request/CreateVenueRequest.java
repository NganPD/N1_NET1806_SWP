package online.be.model.Request;

import lombok.Data;
import online.be.enums.VenueStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
public class CreateVenueRequest {
    private String venueName;
    private String address;
    private VenueStatus venueStatus;
    private String contactInfor;
    private String openingHours;
    private String closingHours;
    private String description;
    private String services;
    private long managerId;//reference từ account
}
