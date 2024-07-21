package online.be.model;

import lombok.Data;
import online.be.enums.SlotStatus;

import java.time.LocalDate;

@Data
public class CheckedSlotDetail {
    private long  courtTimeSlotId;
    private LocalDate checkInDate;
    private SlotStatus slotStatus;
}
