package online.be.model.Response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import online.be.entity.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class VenueResponse extends Venue {
    private String operatingHours;
    private int numberOfCourt;
    private double fixedPrice;
    private double dailyPrice;
    private double flexiblePrice;
    private double rating;
}
