package online.be.model.Request;

import lombok.Data;
import online.be.enums.CourtStatus;

@Data
public class UpdateCourtRequest {
    private String courtName;
    private String description;
    private CourtStatus status;
}
