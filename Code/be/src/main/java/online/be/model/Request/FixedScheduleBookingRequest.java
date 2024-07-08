package online.be.model.Request;

import lombok.Data;

import java.util.List;

@Data
public class FixedScheduleBookingRequest {
    private String applicationStartDate;
    private List<FixedTimeSlot> fixedTimeSlots;
    private int durationInMonths;

    @Data
    public static class FixedTimeSlot {
        private String dayOfWeek;
        private long court;
        private List<Long> timeslot;
    }
}
