package online.be.model.Request;

import lombok.Data;
import online.be.enums.BookingType;

import java.time.LocalDate;
import java.util.List;

@Data
public class FlexibleBookingRequest {
    private String bookingDate;
    private int totalHours;
    private List<BookingDetailRequest> bookingDetailRequests;
}
