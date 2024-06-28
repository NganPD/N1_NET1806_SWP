package online.be.model.Request;

import lombok.Data;

import java.util.List;
@Data
public class CourtScheduleRequest {

    private String status;

    private String date;

    private long courtId;

    private List<Long> timeSlotsId;
}
