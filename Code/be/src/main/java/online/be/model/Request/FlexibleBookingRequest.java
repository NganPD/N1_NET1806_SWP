package online.be.model.Request;

import lombok.Data;
import online.be.entity.TimeSlot;
import online.be.enums.BookingType;

import java.time.LocalDate;
import java.util.List;

@Data
public class FlexibleBookingRequest {
    private int totalHours;
    private String startDate;
    private long courtId;
    private List<TimeSlot> selectedTimeSlots;
}
