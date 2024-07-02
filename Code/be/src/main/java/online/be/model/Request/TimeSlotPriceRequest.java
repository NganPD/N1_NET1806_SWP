package online.be.model.Request;

import lombok.Data;
import online.be.enums.BookingType;

@Data
public class TimeSlotPriceRequest {
    private BookingType bookingType;
    private double price;
}
