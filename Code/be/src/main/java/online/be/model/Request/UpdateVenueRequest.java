package online.be.model.Request;

import lombok.Data;
import online.be.enums.VenueStatus;

import java.time.LocalTime;
import java.util.List;

@Data
public class UpdateVenueRequest {
    private String venueName;
    private String address;
    private String contactInfor;
    private VenueStatus venueStatus;
    private String operatingHours;
    private String closingHours;
    private String services;
    private String imageUrl;
    private String description;
}
