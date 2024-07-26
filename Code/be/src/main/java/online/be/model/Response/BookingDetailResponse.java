package online.be.model.Response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDetailResponse {
    private Long detailId;
    private LocalDate checkIndate;
    private Long courtId;
    private Long timeSlotId;
    private int duration;
}
