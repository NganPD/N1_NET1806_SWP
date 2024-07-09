package online.be.model.Request;

import lombok.Data;
import online.be.enums.CourtStatus;

@Data
public class CreateCourtRequest {
    private String courtName;
    private CourtStatus status;
    private String description;
    private String services;
    private long venueId;
}
