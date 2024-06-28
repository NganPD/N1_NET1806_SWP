package online.be.model.Request;

import lombok.Data;
import online.be.enums.VenueStatus;

import java.time.LocalTime;

@Data
public class CreateVenueRequest {
    private String venueName;
    private String address;
    private VenueStatus venueStatus;
    private LocalTime operatingHours;
    private LocalTime closingHours;
    private String description;
    private String services;
    private Long managerId;//reference từ account
}
