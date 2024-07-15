package online.be.model.Request;

import lombok.Data;
import online.be.enums.BookingType;

@Data
public class PricingRequest {
    private long timeSlotId;
    private long venueId;
    private double price;
    private BookingType bookingType;
}
