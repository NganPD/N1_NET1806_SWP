package online.be.model.Request;

import lombok.Data;
import online.be.enums.CourtStatus;
import online.be.enums.CourtType;

@Data
public class UpdateCourtRequest {
    private Long courtId;
    private String courtName;
    private CourtType courtType;
    private String description;
    private CourtStatus status;
    private String amenities;
    private Long venueId;
}
