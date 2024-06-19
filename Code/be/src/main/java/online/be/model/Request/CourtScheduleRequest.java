package online.be.model.Request;

import lombok.Data;

import java.util.List;
@Data
public class CourtScheduleRequest {

    private String status;

    private String date;

    private Long courtId;

    private List<Long> timeSlotsId;
}
