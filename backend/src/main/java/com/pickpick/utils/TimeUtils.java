package com.pickpick.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {

    private TimeUtils() {
    }

    public static LocalDateTime toLocalDateTime(final String unixTime) {
        String time = unixTime.replace(".", "")
                .substring(0, 13);
        return toLocalDateTime(Long.parseLong(time));
    }

    private static LocalDateTime toLocalDateTime(final long unixTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(unixTime), ZoneId.of("Asia/Seoul"));
    }
}
