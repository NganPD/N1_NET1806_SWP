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

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(()-> new BadRequestException("Booking ID not found"));

        List<TimeSlotPrice> prices = timeSlotPriceRepository.findByBookingTypeAndTimeSlotId(BookingType.FLEXIBLE, courtTimeSlot.getTimeSlot().getId());
        if (prices.isEmpty()) {
            throw new BadRequestException("No price found for the given time slot and booking type");
        }

        // Kiểm tra nếu đã có BookingDetail với cùng booking_id, courtTimeSlot_id và date
        BookingDetail existingDetail = bookingDetailRepostiory.findByBookingAndCourtTimeSlotAndDate(
                booking, courtTimeSlot, request.getCheckInDate());

        if (existingDetail != null) {
            // Nếu đã tồn tại, có thể cập nhật hoặc ném lỗi tùy thuộc vào logic của bạn
            throw new BadRequestException("BookingDetail already exists for this booking, court time slot, and date");
        }

        double price = prices.get(0).getPrice();
        long duration = courtTimeSlotRepository.findDurationByCourTimeSlotId(request.getCourtTimeSlotId());
        BookingDetail detail = new BookingDetail();
        detail.setPrice(price);
        detail.setDuration(duration);
        detail.setDate(request.getCheckInDate());
        detail.setStatus(BookingStatus.PENDING);
        detail.setCourtTimeSlot(courtTimeSlot);
        detail.setBooking(booking);

        courtTimeSlot.setStatus(SlotStatus.BOOKED);
        courtTimeSlotRepository.save(courtTimeSlot);

        bookingDetailRepostiory.save(detail);
        //Update total price in booking
        Double totalPrice = bookingDetailRepostiory.findTotalPriceByBookingId(booking.getId());
        if(totalPrice != null){
            booking.setTotalPrice(totalPrice);
            bookingRepository.save(booking);
        }
        return detail;
    }
}
