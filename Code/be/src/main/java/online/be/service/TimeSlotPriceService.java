package online.be.service;

import online.be.entity.TimeSlot;
import online.be.entity.TimeSlotPrice;
import online.be.exception.BadRequestException;
import online.be.model.Request.TimeSlotPriceRequest;
import online.be.model.Response.TimeSlotPriceResponse;
import online.be.repository.TimeSlotPriceRepository;
import online.be.repository.TimeSlotRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSlotPriceService {

    @Autowired
    TimeSlotPriceRepository timeSlotPriceRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    public TimeSlotPriceResponse createTimeSlot(TimeSlotPriceRequest request){
        //check the timeslot id exist or not
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new BadRequestException("The time slot does not exist"));
        //if the timeslot exist set the field price
        //create a new timeslotprice entity
        TimeSlotPrice slotPrice = new TimeSlotPrice();
        slotPrice.setBookingType(request.getBookingType());
        slotPrice.setDiscount(request.getDiscount());
        slotPrice.setPrice(request.getPrice());
        slotPrice.setTimeSlot(timeSlot);

        TimeSlotPrice createdSlotPrice = timeSlotPriceRepository.save(slotPrice);

        //create and return the response
        TimeSlotPriceResponse response = new TimeSlotPriceResponse();
        response.setTimeSlotId(createdSlotPrice.getTimeSlot().getId());
        response.setBookingType(createdSlotPrice.getBookingType());
        response.setPrice(createdSlotPrice.getPrice());
        return response;
    }

    public TimeSlotPriceResponse updateTimeSlotPrice(long timeSlotId, long timeSlotPriceId, TimeSlotPrice timeSlotPrice){
        //check the timeslot id
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new BadRequestException("The time slot does not exist"));
        //check the time slot price id
        TimeSlotPrice existingTimeSlotPrice = timeSlotPriceRepository.findById(timeSlotPriceId)
                .orElseThrow(()-> new BadRequestException("The time slot price id does not exist"));

        existingTimeSlotPrice.setTimeSlot(timeSlot);
        existingTimeSlotPrice.setBookingType(timeSlotPrice.getBookingType());
        existingTimeSlotPrice.setPrice(timeSlotPrice.getPrice());

        TimeSlotPrice updateTimeSlotPrice = timeSlotPriceRepository.save(existingTimeSlotPrice);

        //return the response
        TimeSlotPriceResponse response = new TimeSlotPriceResponse();
        response.setTimeSlotId(updateTimeSlotPrice.getTimeSlot().getId());
        response.setBookingType(updateTimeSlotPrice.getBookingType());
        response.setPrice(updateTimeSlotPrice.getPrice());

        return response;
    }

    public List<TimeSlotPrice> getAllSlotPrice(){
        return timeSlotPriceRepository.findAll();
    }


}
