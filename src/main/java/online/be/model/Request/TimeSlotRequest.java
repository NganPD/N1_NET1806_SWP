package online.be.model.Request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotRequest {

    private int duration; // in minutes

    private LocalTime startTime;

    private LocalTime endTime;

    private long courtScheduleId;
}
