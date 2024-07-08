package online.be.service;

import online.be.entity.*;
import online.be.enums.BookingType;
import online.be.enums.SlotStatus;
import online.be.exception.ResourceNotFoundException;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BookingDetailService {

    @Autowired
    BookingDetailRepostiory detailRepo;

    @Autowired
    CourtTimeSlotRepository courtTimeSlotRepo;

    @Autowired
    CourtRepository courtRepo;

    @Autowired
    TimeSlotPriceService timeSlotPriceService;

    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    TimeSlotRepository slotRepo;

    public BookingDetail createBookingDetail(BookingType bookingType, String date, long courtId, long slotId){
        // Create a new Court time slot
        CourtTimeSlot courtTimeSlot = new CourtTimeSlot();
        //Retrieve Time slot and court, catch ResourceNotFoundException
        TimeSlot slot = slotRepo.findById(slotId).orElseThrow(() ->
                new RuntimeException("TimeSlot not found with id: " + slotId));
        Court court = courtRepo.findById(courtId).orElseThrow(() ->
                new RuntimeException("Court not found with id: " + courtId));
        try {
            // Parse the check-in date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate checkInDate = LocalDate.parse(date, formatter);
            courtTimeSlot.setTimeSlot(slot);
            courtTimeSlot.setCourt(court);
            courtTimeSlot.setCheckInDate(checkInDate);
            courtTimeSlot.setStatus(SlotStatus.BOOKED);
            courtTimeSlotRepo.save(courtTimeSlot);
        } catch (Exception e){
            throw new RuntimeException("Something went wrong, please try again!");
        }
        List<TimeSlotPrice> slotPrices = timeSlotPriceService.getAllSlotPriceBySlotId(slot.getId());
        double price = 0;
        for (TimeSlotPrice slotPrice : slotPrices){
            if (slotPrice.getBookingType().equals(bookingType)){
                price = slotPrice.getPrice();
                if (price <= 0){
                    throw new RuntimeException("Price is not negative number and larger than 0");
                }
                price = price - (price * (slotPrice.getDiscount()/100));
            }
        }

        // Create and populate the BookingDetail object
        BookingDetail detail = new BookingDetail();
        try {
            detail.setDuration(slot.getDuration());
            detail.setPrice(price);
            detail.setCourtTimeSlot(courtTimeSlot);
            courtTimeSlot.setStatus(SlotStatus.BOOKED);
        }catch (Exception e){
            throw new RuntimeException("Something went wrong, please try again");
        }
        // Save and return the BookingDetail
        return detail;
    }
}
