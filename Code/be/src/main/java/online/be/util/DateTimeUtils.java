package online.be.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeUtils {

    /**
     * Định dạng LocalDate thành chuỗi sử dụng định dạng DATE_FORMATTER.
     *
     * @param date LocalDate đối tượng
     * @return Chuỗi định dạng ngày
     */
    public String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
