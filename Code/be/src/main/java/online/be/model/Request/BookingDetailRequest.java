package online.be.model.Request;

import lombok.Data;

import java.util.Date;


@Data
public class BookingDetailRequest {
    private Long slotId;
    private Date startTime;
    private Date endTime;
    private Date date;
    private String status;
}
