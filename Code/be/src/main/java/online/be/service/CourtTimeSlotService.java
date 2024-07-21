
package online.be.service;

import online.be.entity.Court;
import online.be.entity.CourtTimeSlot;
import online.be.entity.TimeSlot;
import online.be.enums.SlotStatus;
import online.be.exception.BadRequestException;
import online.be.model.Request.CourtTimeSlotRequest;
import online.be.repository.CourtRepository;
import online.be.repository.CourtTimeSlotRepository;
import online.be.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CourtTimeSlotService {

    @Autowired
    CourtTimeSlotRepository courtTimeSlotRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    CourtRepository courtRepository;

    //create
    public CourtTimeSlot createCourtTimeSlot(CourtTimeSlotRequest request){
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(()-> new BadRequestException("TimeSlot ID not found"));
        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(()-> new BadRequestException("Court ID not found"));
        CourtTimeSlot courtTimeSlot = new CourtTimeSlot();
        courtTimeSlot.setTimeSlot(timeSlot);
        courtTimeSlot.setCourt(court);
        courtTimeSlot.setStatus(request.getStatus());

        return courtTimeSlotRepository.save(courtTimeSlot);
    }

    public CourtTimeSlot getCourtTimeSlotById(long courtTimeSlotId){
        return courtTimeSlotRepository.findById(courtTimeSlotId);
    }

    public List<CourtTimeSlot> getAll(){
        return courtTimeSlotRepository.findAll();
    }

    public List<CourtTimeSlot> getBookedSlot(long venueId){
        List<CourtTimeSlot> slots = courtTimeSlotRepository.findByStatusAndVenueId(SlotStatus.BOOKED, venueId);
        return slots;
    }

    public List<CourtTimeSlot> getBookedAndCheckedByVenue(long venueId){
        LocalDate today = LocalDate.now();
        List<CourtTimeSlot> courtTimeSlots = courtTimeSlotRepository.findByVenueIdAndDateAndStatus(venueId, today);

        return courtTimeSlots;
    }


}
