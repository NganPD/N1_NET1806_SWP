package online.be.model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import online.be.entity.TimeSlot;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TimeSlotAvabilityResponse {
    private TimeSlot timeSlot;
    private double price;
    private boolean isFullyBooked;
}
