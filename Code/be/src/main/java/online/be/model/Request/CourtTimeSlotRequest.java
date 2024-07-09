package online.be.model.Request;

import lombok.Data;
import online.be.enums.SlotStatus;

@Data
public class CourtTimeSlotRequest {
    private long courtId;
    private long timeSlotId;
    private SlotStatus status;
}
