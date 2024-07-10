package online.be.service;

import online.be.entity.TimeSlot;
import online.be.entity.Discount;
import online.be.exception.BadRequestException;
import online.be.model.Request.DiscountRequest;
import online.be.model.Response.TimeSlotPriceResponse;
import online.be.repository.DiscountRepository;
import online.be.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    public Discount createDiscountTable(DiscountRequest request){
        //check the timeslot id exist or not
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotID())
                .orElseThrow(() -> new BadRequestException("The time slot does not exist"));
        //if the timeslot exist set the field price
        //create a new timeslotprice entity
        Discount slotPrice = new Discount();
        slotPrice.setBookingType(request.getBookingType());
        slotPrice.setDiscount(request.getDiscount());
        slotPrice.setTimeSlot(timeSlot);
        return discountRepository.save(slotPrice);
    }

    public Discount updateTimeSlotPrice(long timeSlotId, long discountId, DiscountRequest request){
        //check the timeslot id
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new BadRequestException("The time slot does not exist"));
        //check the time slot price id
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(()-> new BadRequestException("The time slot price id does not exist"));

        discount.setTimeSlot(timeSlot);
        discount.setBookingType(request.getBookingType());
        discount.setDiscount(request.getDiscount());
        return discountRepository.save(discount);
    }

    public List<Discount> getAllSlotPrice(){
        return discountRepository.findAll();
    }

    public List<Discount> getAllSlotPriceBySlotId(long slotId){
        return discountRepository.findAll();
    }

}
