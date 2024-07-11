package online.be.model.Response;

import lombok.Data;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private String paymentUrl;
}