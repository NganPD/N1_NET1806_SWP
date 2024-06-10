package online.be.model.Request;

import lombok.Data;

@Data
public class VenueRequest {
    private String venueName;
    private String address;
    private String operatingHours;
    private String paymentInfor;
    private String description;
    private String imageURL;
    private int numberOfCourts;
}
