package online.be.model.Response;

import lombok.Data;
import online.be.enums.BookingStatus;
import online.be.model.CheckedSlotDetail;

import java.util.List;

@Data
public class CheckInResponse {
    private long bookingId;
    private BookingStatus bookingStatus;
    private String phoneNumber;
    private String email;
    private List<CheckedSlotDetail> checkedSlotDetails;
}
