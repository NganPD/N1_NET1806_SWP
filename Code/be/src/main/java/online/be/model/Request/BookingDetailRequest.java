package online.be.model.Request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;


@Data
public class BookingDetailRequest {
    private Long courtTimeSlotId;
    private String checkInDate;
}