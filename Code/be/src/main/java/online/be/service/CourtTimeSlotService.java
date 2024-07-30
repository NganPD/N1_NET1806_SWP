
package online.be.service;

import online.be.entity.*;
import online.be.enums.SlotStatus;
import online.be.exception.BadRequestException;
import online.be.model.Request.CourtTimeSlotRequest;
import online.be.model.Response.CourtTimeSlotResponse;
import online.be.repository.CourtRepository;
import online.be.repository.CourtTimeSlotRepository;
import online.be.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourtTimeSlotService {

    @Autowired
    CourtTimeSlotRepository courtTimeSlotRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    CourtRepository courtRepository;

    @Autowired
    AuthenticationService authenticationService;

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

    public List<CourtTimeSlotResponse> getBookedAndCheckedByVenue(){
        LocalDate today = LocalDate.now();
        Account staff = authenticationService.getCurrentAccount();
        Venue venue = staff.getStaffVenue();
        if(venue == null){
            throw new BadRequestException("Venue not found");
        }
        List<CourtTimeSlot> courtTimeSlots = courtTimeSlotRepository.findByVenueIdAndDateAndStatus(venue.getId(), today);
        List<CourtTimeSlotResponse> courtTimeSlotResponses = new ArrayList<>();
        for (CourtTimeSlot courtTimeSlot : courtTimeSlots){
            CourtTimeSlotResponse response = convertToDTO(courtTimeSlot);
            courtTimeSlotResponses.add(response);
        }
        return courtTimeSlotResponses;
    }

    private boolean isExistedCourtTimeSlot(Long timeSlotId, Long courtId, LocalDate date){
        return courtTimeSlotRepository.existsByTimeSlotAndCourt(timeSlotId, courtId, date);
    }

    public CourtTimeSlot checkIn(long courtTimeSlotId){
        CourtTimeSlot courtTimeSlot = courtTimeSlotRepository.findById(courtTimeSlotId);
        if (courtTimeSlot == null){
            throw new BadRequestException("Court Time SLot ID not found: " + courtTimeSlotId);
        }
        if(courtTimeSlot.getStatus() == SlotStatus.BOOKED){
            courtTimeSlot.setStatus(SlotStatus.CHECKED);
            return courtTimeSlotRepository.save(courtTimeSlot);
        }else{
            throw new BadRequestException("Cannot check-in: Slot is not in BOOKED status");
        }
    }

    private CourtTimeSlotResponse convertToDTO(CourtTimeSlot courtTimeSlot) {
        CourtTimeSlotResponse dto = new CourtTimeSlotResponse();
        dto.setBooker(courtTimeSlot.getBookingDetail().getBooking().getAccount().getFullName());
        dto.setDate(courtTimeSlot.getCheckInDate());
        dto.setPrice(courtTimeSlot.getBookingDetail().getPrice());
        dto.setStartTime(courtTimeSlot.getTimeSlot().getStartTime());
        dto.setEndTime(courtTimeSlot.getTimeSlot().getEndTime());
        dto.setCourtName(courtTimeSlot.getCourt().getCourtName());
        dto.setPhoneNumber(courtTimeSlot.getBookingDetail().getBooking().getAccount().getPhone());
        dto.setStatus(courtTimeSlot.getStatus());
        dto.setCourtTimeSlotId(courtTimeSlot.getId());
        return dto;
    }


}
