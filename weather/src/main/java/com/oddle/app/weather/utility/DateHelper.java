package com.oddle.app.weather.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    private DateHelper() {
        throw new IllegalStateException("Error DateHelper class");
    }

    /**
     * Format LocalDateTime into formatted local date in String
     * @param dateTime date time value
     * @return formatted date time
     */
    public static String getFormattedLocalDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return dateTime.format(formatter);
    }

    public static LocalDateTime setFormattedDate(String date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return LocalDate.parse(date, formatter).atStartOfDay();
    }
}
