package online.be.model.Response;

import lombok.Data;
import online.be.enums.BookingType;

@Data
public class TimeSlotPriceResponse {
    private long timeSlotId;
    private BookingType bookingType;
    private double price;
}
