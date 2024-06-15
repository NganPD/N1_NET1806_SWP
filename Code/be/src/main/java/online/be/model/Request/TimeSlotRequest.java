package online.be.model.Request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotRequest {

    private LocalTime startTime;

    private LocalTime endTime;

    private int duration; // in minutes

}
