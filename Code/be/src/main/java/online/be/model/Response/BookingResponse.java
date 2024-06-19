package online.be.model.Response;

import lombok.Data;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;

@Data
public class BookingResponse {
    private Long bookingId;
    private String message;
    private BookingStatus bookingStatus;
}
