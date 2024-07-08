package online.be.model.Response;

import lombok.Data;
import online.be.enums.BookingStatus;

@Data
public class BookingResponse {
    private long bookingId;
    private String msg;
    private BookingStatus bookingStatus;
}