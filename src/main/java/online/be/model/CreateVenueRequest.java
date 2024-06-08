package online.be.model;

import lombok.Data;

@Data
public class CreateVenueRequest {

    private String name;
    private String address;
    private String description;
    private String operatingHours;
    private String paymentInfor;
    private String imageURL;
    private Integer numberOfCourts;
}
