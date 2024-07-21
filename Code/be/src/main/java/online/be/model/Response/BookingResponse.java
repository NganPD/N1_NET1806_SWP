package online.be.model.Response;

import lombok.Data;
import online.be.entity.Booking;
import online.be.entity.Venue;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponse {
    private long id;
    private LocalDate bookingDate;
    private LocalDate applicationDate;
    private int totalTimes;
    private double totalPrice;
    private Long venueId;
    private String venueName;
    private int remainingTimes;
    private BookingType bookingType;
    private BookingStatus status;
    private Long accountId;
    private String accountName;
    private int purchasedTime;//thời gian đã mua
    private List<BookingDetailResponse> bookingDetails;
    private boolean isCancel;
}