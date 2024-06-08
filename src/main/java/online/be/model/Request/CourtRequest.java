package online.be.model.Request;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import online.be.entity.Venue;
@Data
public class CourtRequest {
    private String courtName;

    private String location;

    private String operatingHours;

    private String description;
    private String paymentInfo;
    private int numberOfCourts;

    private long venueId;
}
