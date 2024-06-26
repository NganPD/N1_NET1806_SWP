package online.be.model.Request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateScheduleRequest {

    private LocalDate date;


    private boolean available;


    private long timeSlotId;


    private long courtId;


}
