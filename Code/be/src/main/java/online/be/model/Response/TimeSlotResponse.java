package online.be.model.Response;

import lombok.Data;

import java.time.LocalTime;

@Data

public class TimeSlotResponse {

    long id;

    String startTime;

    String endTime;

    long duration;

    boolean available;
}
