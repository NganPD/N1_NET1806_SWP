package online.be.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTImeUtils {
    // Định dạng ngày: dd-MM-yyyy
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Định dạng LocalDate thành chuỗi sử dụng định dạng DATE_FORMATTER.
     *
     * @param date LocalDate đối tượng
     * @return Chuỗi định dạng ngày
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
}
