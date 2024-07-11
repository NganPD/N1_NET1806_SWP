package online.be.model.Response;

import lombok.Data;
import lombok.Getter;

import java.time.LocalTime;

@Data
@Getter
public class TimeSlotResponse {

    long id;

    String startTime;

    String endTime;

    long duration;

    boolean available;

    double price;
}
