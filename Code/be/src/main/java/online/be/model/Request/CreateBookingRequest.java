package online.be.model.Request;

import lombok.Data;
import online.be.enums.BookingType;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateBookingRequest {
    private Long courtId;
    private BookingType bookingType;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long userId;
}
