package online.be.model.Request;

import lombok.Data;

import java.util.List;

@Data
public class DailyScheduleBookingRequest {

    private String checkInDate;

    private long court;

    private List<Long> timeslot;
}
