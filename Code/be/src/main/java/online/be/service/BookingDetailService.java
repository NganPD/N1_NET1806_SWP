package online.be.service;

import online.be.entity.*;
import online.be.enums.BookingStatus;
import online.be.enums.BookingType;

import online.be.enums.SlotStatus;
import online.be.exception.BadRequestException;
import online.be.model.Request.BookingDetailRequest;
import online.be.repository.BookingDetailRepostiory;
import online.be.repository.BookingRepository;
import online.be.repository.CourtTimeSlotRepository;
import online.be.repository.TimeSlotPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BookingDetailService {

    @Autowired
    BookingDetailRepostiory bookingDetailRepostiory;

    @Autowired
    CourtTimeSlotRepository courtTimeSlotRepository;

    @Autowired
    TimeSlotPriceRepository timeSlotPriceRepository;

    @Autowired
    BookingRepository bookingRepository;

    public List<BookingDetail> getByTimeSlotPricesId(long id) {
        return bookingDetailRepostiory.findByCourtTimeSlot_TimeSlot_TimeSlotPricesId(id);
    }

    public BookingDetail createFlexibleBookingDetail(BookingDetailRequest request){
        CourtTimeSlot courtTimeSlot = courtTimeSlotRepository.findById(request.getCourtTimeSlotId())
                .orElseThrow(()-> new BadRequestException("Selected court timeslot not found"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate checkInDate = LocalDate.parse(request.getCheckInDate(),formatter);

        TimeSlotPrice slotPrice = timeSlotPriceRepository.findByBookingTypeAndTimeSlotId(BookingType.FLEXIBLE, courtTimeSlot.getTimeSlot().getId())
                .orElseThrow(() -> new BadRequestException("No price found for the given time slot and booking type"));

        double price = slotPrice.getPrice();
        double discount = slotPrice.getDiscount();

        //calculate the final price after applying the discount
        double finalPrice = price*(1-discount);

        BookingDetail detail = new BookingDetail();
        try{
            long duration = courtTimeSlotRepository.findDurationByCourTimeSlotId(request.getCourtTimeSlotId());
            detail.setDuration(duration);
            detail.setPrice(finalPrice);
            detail.setCourtTimeSlot(courtTimeSlot);
            detail.setCheckInDate(checkInDate);

            courtTimeSlot.setStatus(SlotStatus.BOOKED);
            courtTimeSlotRepository.save(courtTimeSlot);
        }catch (Exception e){
            throw new RuntimeException("Something went wrong, please try again");
        }
        return detail;
    }
}
