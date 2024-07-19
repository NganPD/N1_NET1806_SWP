
package online.be.service;

import online.be.entity.*;
import online.be.enums.BookingType;
import online.be.enums.SlotStatus;
import online.be.exception.BadRequestException;
import online.be.exception.NoDataFoundException;
import online.be.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class BookingDetailService {

    @Autowired
    BookingDetailRepositiory detailRepo;

    @Autowired
    CourtTimeSlotRepository courtTimeSlotRepo;


    public BookingDetail createBookingDetail(BookingType bookingType, LocalDate date, Court court, TimeSlot slot){
        // Create a new Court time slot
        CourtTimeSlot courtTimeSlot = new CourtTimeSlot();
        if (date == null) {
            throw new IllegalArgumentException("Date string is null or empty");
        }
        try {
            courtTimeSlot.setTimeSlot(slot);
            courtTimeSlot.setCourt(court);
            courtTimeSlot.setCheckInDate(date);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new BadRequestException("Failed to parse date or invalid date format: " + e.getMessage());
        } catch (Exception e){
            throw new BadRequestException("CourtTimeSlot cannot be created!");
        }

        // Retrieve price for the booking type
        double price;
        try {
            // Get pricing based on booking type from TimeSlot or Court
            Pricing pricing = slot.getPricingList().stream()
                    .filter(p -> p.getBookingType().equals(bookingType))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Pricing not found for booking type: " + bookingType));

            price = pricing.getPricePerHour();
        } catch (Exception e) {
            throw new BadRequestException("Failed to retrieve price for the given booking type: " + e.getMessage());
        }

        // Create and populate the BookingDetail object
        BookingDetail detail = new BookingDetail();
        try {
            detail.setDuration(slot.getDuration());
            detail.setPrice(price);
            detail.setCourtTimeSlot(courtTimeSlot);
            courtTimeSlot.setStatus(SlotStatus.AVAILABLE);
            courtTimeSlotRepo.save(courtTimeSlot);
        }catch (Exception e){
            throw new BadRequestException("Something went wrong, please try again");
        }
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
        return detailRepo.findById(bookingDetailId)
                .orElseThrow(()-> new BadRequestException("Booking Detail not found"));
    }


}
