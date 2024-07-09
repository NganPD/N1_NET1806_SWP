package online.be.model.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotRequest {

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "HH:mm")
    private String startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "HH:mm")
    private String  endTime;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private double price;

    @NotNull(message = "Status is required")
    private boolean status;


    @NotNull(message = "Venue ID is required")
    private long venueId;
}
