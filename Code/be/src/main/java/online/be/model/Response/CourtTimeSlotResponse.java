package online.be.model.Response;

import lombok.Data;
import online.be.enums.BookingType;
import online.be.enums.SlotStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CourtTimeSlotResponse {
    private String booker;
    private LocalDate date;
    private double price;
    private LocalTime startTime;
    private LocalTime endTime;
    private String courtName;
    private String phoneNumber;
    private SlotStatus status;
}
