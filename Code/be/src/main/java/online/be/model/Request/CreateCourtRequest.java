package online.be.model.Request;

import lombok.Data;
import online.be.enums.CourtStatus;

@Data
public class CreateCourtRequest {
    private String courtName;
    private CourtStatus status;
    private String amenities;
    private String description;
    private long venueId;
}
