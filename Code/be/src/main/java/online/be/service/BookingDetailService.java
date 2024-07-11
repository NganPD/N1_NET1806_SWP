
package online.be.service;

import online.be.entity.*;
import online.be.enums.BookingType;
import online.be.enums.SlotStatus;
import online.be.exception.BadRequestException;
import online.be.exception.NoDataFoundException;
import online.be.repository.*;
import online.be.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    DiscountService discountService;

    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    TimeSlotRepository slotRepo;

    @Autowired
    DateTimeUtils utils;

    public BookingDetail createBookingDetail(BookingType bookingType, String date, Court court, TimeSlot slot){
        // Create a new Court time slot
        CourtTimeSlot courtTimeSlot = new CourtTimeSlot();
//        //Retrieve Time slot and court, catch ResourceNotFoundException
//        TimeSlot slot = slotRepo.findById(slotId).orElseThrow(() ->
//                new BadRequestException("TimeSlot not found with id: " + slotId));
//        Court court = courtRepo.findById(courtId).orElseThrow(() ->
//                new BadRequestException("Court not found with id: " + courtId));
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date string is null or empty");
        }
        try {
            // Parse the check-in date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate checkInDate = LocalDate.parse(date, formatter);
            courtTimeSlot.setTimeSlot(slot);
            courtTimeSlot.setCourt(court);
            courtTimeSlot.setCheckInDate(checkInDate);
            courtTimeSlot.setStatus(SlotStatus.BOOKED);
            courtTimeSlotRepo.save(courtTimeSlot);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to parse date or invalid date format: " + e.getMessage(), e);
        } catch (Exception e){
            throw new RuntimeException("Something went wrong, please try again!");
        }

        double price = slot.getPrice();
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

    public List<BookingDetail> getBookingDetailByBookingId(long bookingId){
        List<BookingDetail> details = detailRepo.findByBookingId(bookingId);
        if(details.isEmpty()){
            throw new NoDataFoundException("Nothing to show");
        }
        return details;
    }

    public BookingDetail getBookingDetailById(long bookingDetailId){
        BookingDetail detail = detailRepo.findById(bookingDetailId)
                .orElseThrow(()-> new BadRequestException("Booking Detail not found"));
        return detail;
    }
}
