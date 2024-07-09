package online.be.model.Request;

import lombok.Data;

@Data
public class VenueReviewRequest {
    private long userId;
    private String comment;
    private int rating;
    private long venueId;
}
