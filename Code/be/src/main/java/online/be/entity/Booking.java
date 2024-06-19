//package online.be.entity;
//
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import online.be.enums.BookingType;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@Entity
//@Getter
//@Setter
//@ToString
//public class Booking {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long bookingId;
//
//    @Column(name = "booking_date", nullable = false)
//    private String bookingDate;
//
//    @Column(name = "hours", nullable = false)
//    private double hours;
//
//    @Column(name = "price", nullable = false)
//    private double price;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "bookingType", nullable = false)
//    private BookingType bookingType;
//
//    @ManyToOne
//    @JoinColumn(name = "user_Id", nullable = false)
//    private Account account;
//
//    @OneToOne
//    @JoinColumn(name = "payment_Id")
//    private Payment payment;
//
//    //THiếu reference của transaction, booking detail và booking history
//    //Xoá reference payment
//}
