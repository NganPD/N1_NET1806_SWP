package online.be.model.Request;

import lombok.Data;

@Data
public class ReviewRequest {
    private String feedback;
    private int rating;
    private long venueId;
}
