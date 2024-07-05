package online.be.model.Request;

import lombok.Data;
import online.be.enums.BookingType;

import java.time.LocalDate;
import java.util.List;

@Data
public class FlexibleBookingRequest {
    private LocalDate bookingDate;
    private int totalHours;
    private long customerId;
}
