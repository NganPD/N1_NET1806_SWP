package online.be.model.Response;

import lombok.Data;
import online.be.entity.*;
import online.be.enums.VenueStatus;

import java.time.LocalTime;
import java.util.List;

@Data
public class VenueResponse {

    private long id;

    private String name;

    private String address;

    private VenueStatus venueStatus;

    private String services;

    private LocalTime operatingHours;

    private LocalTime closingHours;

    private String description;

    private List<Court> courts;

    private List<TimeSlot> timeSlots;

    private List<Review> reviews;

    private Account manager;

    private List<PaymentAccount> paymentInforList;

    private int numberOfCourt;

    private double price;
}
