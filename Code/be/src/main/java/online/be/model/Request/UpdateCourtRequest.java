package online.be.model.Request;

import lombok.Data;
import online.be.enums.CourtStatus;

@Data
public class UpdateCourtRequest {
    private String courtName;
    private CourtStatus status;
    private String amenities;
    private Long venueId;
}
