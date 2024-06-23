package online.be.model.Request;

import lombok.Data;
import online.be.enums.CourtStatus;

@Data
public class UpdateCourtRequest {
    private Long courtId;
    private String courtName;
    private String description;
    private CourtStatus status;
    private String amenities;
    private Long venueId;
}
