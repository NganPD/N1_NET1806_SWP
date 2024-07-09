package online.be.model.Request;

import lombok.Data;
import online.be.enums.BookingType;

import java.util.List;

@Data
public class BookingRequest {
    private long bookingId;
    private BookingType bookingType;
    //Thiết kế lại request theo format của luồng đặt lịch
}
