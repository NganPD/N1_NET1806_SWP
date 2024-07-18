package online.be.model.Response;

import lombok.Data;
import online.be.entity.Booking;
import online.be.entity.Venue;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingResponse extends Booking {
    private long venueId;
    private String paymentURL;
}