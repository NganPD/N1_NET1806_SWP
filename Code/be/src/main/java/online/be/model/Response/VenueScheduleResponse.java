package online.be.model.Response;

import lombok.Data;
import online.be.entity.Pricing;
import online.be.entity.TimeSlot;
import online.be.entity.Venue;

import java.util.List;

@Data
public class VenueScheduleResponse {
    private Venue venue;
    private List<TimeSlot> timeSlots;
    private List<Pricing> pricingList;

}
