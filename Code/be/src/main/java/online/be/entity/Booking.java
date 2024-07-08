    package online.be.entity;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;
    import lombok.ToString;
    import online.be.enums.BookingStatus;
    import online.be.enums.BookingType;

    import java.time.LocalDate;
    import java.util.List;

    @Entity
    @Getter
    @Setter
    @ToString
    public class Booking {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        @JsonFormat
        private LocalDate bookingDate;
        private int totalTimes;
        private double totalPrice;
        private int remainingTimes;//flexible booking

        @Enumerated(EnumType.STRING)
        private BookingType bookingType;

        private LocalDate checkinDate;

        @Enumerated(EnumType.STRING)
        private BookingStatus status;

        @ManyToOne
        @JoinColumn(name = "user_Id")
        private Account account;

        @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
        @JsonIgnore
        private List<BookingDetail> bookingDetailList;

        @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
        private List<Transaction> transactions;
    }
