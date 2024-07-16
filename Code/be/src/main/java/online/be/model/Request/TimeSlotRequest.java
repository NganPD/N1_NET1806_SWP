package online.be.model.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotRequest {

    @JsonFormat(pattern = "HH:mm")
    private String startTime;

    @JsonFormat(pattern = "HH:mm")
    private String  endTime;

    @NotNull(message = "Venue ID is required")
    private long venueId;
}
