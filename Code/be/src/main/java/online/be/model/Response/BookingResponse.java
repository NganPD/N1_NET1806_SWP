package online.be.model.Response;

import lombok.Data;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;
import online.be.enums.PaymentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private Long bookingId;
    private LocalDate bookingDate;
    private double totalHours;
    private double totalPrice;
    private BookingType bookingType;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private String paymentUrl;
}