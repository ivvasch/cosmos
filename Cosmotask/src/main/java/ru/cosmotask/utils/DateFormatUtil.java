package ru.cosmotask.utils;

import com.google.api.client.util.DateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {
    private DateFormatUtil() {
    }

    public static DateTime localDateTimeToDateTimeGoogleFormat(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = localDateTime.format(formatter);
        String replaced = format.replace(" ", "T");
        return new DateTime(replaced);
    }
}
