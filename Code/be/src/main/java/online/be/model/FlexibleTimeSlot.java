package online.be.model;

import lombok.Data;

import java.util.List;

@Data
public class FlexibleTimeSlot {
    private String checkInDate;
    private long court;
    private List<Long> timeslot;
}
