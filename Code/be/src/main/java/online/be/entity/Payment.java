//package online.be.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import online.be.enums.PaymentStatus;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//@ToString
//public class Payment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long paymentId;
//
//    @Column(nullable = false)
//    private String paymentMethod;
//
//    @Column
//    private double Amount;
//
//    @Enumerated(EnumType.STRING)
//    private PaymentStatus status;
//
//    @Column(nullable = false)
//    private LocalDate paymentDate;
//
//    @OneToOne
//    @JoinColumn(name = "booking_Id")
//    private Booking booking;
//
//}
