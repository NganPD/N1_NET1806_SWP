package online.be.model.Request;

import lombok.Data;

import java.util.List;

@Data
public class FixedScheduleBookingRequest {
    private String applicationStartDate;
    private int durationInMonths;
    private List<String> dayOfWeek;
    private long court;
    private List<Long> timeslot;
}
