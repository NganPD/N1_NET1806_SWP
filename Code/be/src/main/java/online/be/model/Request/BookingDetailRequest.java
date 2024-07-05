package online.be.model.Request;

import lombok.Data;

@Data
public class BookingDetailRequest {

    private String checkInDate;

    private long courtTimeSlot;
}
