package online.be.model.Request;

import lombok.Data;

import java.util.List;

@Data
public class DailyScheduleBookingRequest {

    private String bookingDate;

    private long accountId;

    private BookingDetailRequest bookingDetailRequests;
}
