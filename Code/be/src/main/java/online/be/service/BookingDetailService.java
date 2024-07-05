package online.be.service;

import online.be.entity.*;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;
import online.be.enums.SlotStatus;
import online.be.exception.ResourceNotFoundException;
import online.be.model.Request.BookingDetailRequest;
import online.be.repository.BookingDetailRepostiory;
import online.be.repository.BookingRepository;
import online.be.repository.CourtTimeSlotRepository;
import online.be.repository.TimeSlotRepository;
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
    TimeSlotPriceService timeSlotPriceService;

    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    BookingRepository bookingRepo;

    public BookingDetail createBookingDetail(BookingDetailRequest detailRequest){
        // Retrieve CourtTimeSlot, throwing an exception if not found
        CourtTimeSlot courtTimeSlot = courtTimeSlotRepo.findById(detailRequest.getCourtTimeSlot())
                .orElseThrow(() -> new ResourceNotFoundException("Court Time Slot does not exist!"));

        // Parse the check-in date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate checkInDate = LocalDate.parse(detailRequest.getCheckInDate(), formatter);
        TimeSlot slot = courtTimeSlot.getTimeSlot();
        List<TimeSlotPrice> slotPrices = timeSlotPriceService.getAllSlotPriceBySlotId(slot.getId());
        double price = 0;
        for (TimeSlotPrice slotPrice : slotPrices){
            if (slotPrice.getBookingType().equals(BookingType.DAILY)){
                price = slotPrice.getPrice();
                if (price <= 0){
                    throw new RuntimeException("Something went wrong, please try again");
                }
                price = price - (price * slotPrice.getDiscount());
            }
        }

        // Create and populate the BookingDetail object
        BookingDetail detail = new BookingDetail();
        try {
            detail.setDuration(slot.getDuration());
            detail.setPrice(price);
            detail.setCourtTimeSlot(courtTimeSlot);
            detail.setCheckInDate(checkInDate); // Assuming you have a setCheckInDate method
            courtTimeSlot.setStatus(SlotStatus.BOOKED);
        }catch (Exception e){
            throw new RuntimeException("Something went wrong, please try again");
        }

        // Save and return the BookingDetail
        return detail;
    }
}
