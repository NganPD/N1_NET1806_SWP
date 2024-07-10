package online.be.model.Response;

import lombok.Data;
import online.be.entity.*;
import online.be.enums.VenueStatus;
import online.be.exception.VenueException;

import java.time.LocalTime;
import java.util.List;

@Data
public class VenueResponse extends Venue {
    private String operatingHours;
    private int numberOfCourt;
    private double price;
}
