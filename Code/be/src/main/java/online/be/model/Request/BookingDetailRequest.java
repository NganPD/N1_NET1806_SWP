package online.be.model.Request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;


@Data
public class BookingDetailRequest {
    private long courtTimeSlotId;
    private LocalDate checkInDate;
    private long bookingId;
}