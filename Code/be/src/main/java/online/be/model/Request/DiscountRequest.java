package online.be.model.Request;

import lombok.Data;
import online.be.enums.BookingType;

@Data
public class DiscountRequest {
    private long timeSlotID;
    private BookingType bookingType;
    private double discount;
}
