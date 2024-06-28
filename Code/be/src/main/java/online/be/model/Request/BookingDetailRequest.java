package online.be.model.Request;

import lombok.Data;

import java.util.Date;


@Data
public class BookingDetailRequest {
    private long bookingId;
    private long scheduleId;
}
