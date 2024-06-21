package online.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import online.be.enums.BookingType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingId;

    @Column(name = "booking_date", nullable = false)
    private String bookingDate;

    @Column(name = "hours", nullable = false)
    private double totalHours;

    @Column(name = "price", nullable = false)
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "bookingType", nullable = false)
    private BookingType bookingType;

    @Column(name = "checkin_date")
    private LocalDateTime checkinDate;

    @Column(name = "checkout_date")
    private LocalDateTime checkoutDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_Id", nullable = false)
    private Account account;

    @OneToOne
    @JoinColumn(name = "bookingHistoryId", nullable = false)
    private BookingHistory bookingHistory;

    @OneToMany(mappedBy = "booking")
    private List<BookingDetail> bookingDetailList;

    // Other properties or methods if needed
}
