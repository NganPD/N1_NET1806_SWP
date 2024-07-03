package online.be.model.Request;

import lombok.Data;
import online.be.entity.Account;
import online.be.entity.BookingDetail;
import online.be.entity.Payment;
import online.be.enums.BookingType;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {
    private BookingType bookingType;
    private long accountId;
    private List<BookingDetailRequest> bookingDetailRequests;
    //Thiết kế lại request theo format của luồng đặt lịch
}
