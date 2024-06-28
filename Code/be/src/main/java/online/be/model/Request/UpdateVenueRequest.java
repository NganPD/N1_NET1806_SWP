package online.be.model.Request;

import lombok.Data;
import online.be.enums.VenueStatus;

import java.time.LocalTime;
import java.util.List;

@Data
public class UpdateVenueRequest {
    private String venueName;
    private String address;
    private VenueStatus venueStatus;
    private LocalTime operatingHours;
    private LocalTime closingHours;
    private String description;
    private List<Long> assignedCourts;
}
