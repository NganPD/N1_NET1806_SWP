package online.be.model.Request;

import lombok.Data;
import online.be.entity.Account;
import online.be.entity.Payment;
import online.be.enums.BookingType;

@Data
public class BookingRequest {
    private String bookingDate;
    private double price;
    private double hours;
    private BookingType bookingType;
    private Account account;
    private Payment payment;
}
