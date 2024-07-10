package online.be.model.Request;

import lombok.Data;
import online.be.entity.TimeSlot;
import online.be.enums.BookingType;

import java.time.LocalDate;
import java.util.List;

@Data
public class FlexibleBookingRequest {
    private long bookingId;
    private List<FlexibleTimeSlot> flexibleTimeSlots;
    @Data
    public static class FlexibleTimeSlot {
        private String checkInDate;
        private long court;
        private List<Long> timeslot;
    }
}
