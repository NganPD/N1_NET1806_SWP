package online.be.model.Request;

import lombok.Data;

import java.util.List;

@Data
public class FixedScheduleBookingRequest {
    private String applicationStartDate;
    private int durationInMonths;
    private List<FixedTimeSlot> fixedTimeSlots;

    @Data
    public static class FixedTimeSlot {
        private String dayOfWeek;
        private long court;
        private List<Long> timeslot;
    }
}
