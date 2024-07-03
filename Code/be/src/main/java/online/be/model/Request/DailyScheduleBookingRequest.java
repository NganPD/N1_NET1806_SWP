package online.be.model.Request;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DailyScheduleBookingRequest {
    private LocalDate bookingDate;
    private LocalDateTime checkInDate;
    private long venueId;
    private long timeSlotId;
    private long accountId;
}
