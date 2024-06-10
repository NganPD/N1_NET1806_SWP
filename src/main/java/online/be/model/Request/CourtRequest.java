package online.be.model.Request;

import lombok.Data;

@Data
public class CourtRequest {

    private String courtName;

    private boolean status;

    private String amenities;

    private long venueId;
}
