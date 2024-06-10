package online.be.model.Request;

import lombok.Data;
import online.be.enums.BookingType;

@Data
public class BookingRequest {
    private long id; //Account's id
    private BookingType bookingType;
    private long courtScheduleId;
}
