package online.be.model;

import lombok.Data;

@Data
public class UpdateCourtRequest {
    private String name;
    private String status;
    private String amenities;
}
