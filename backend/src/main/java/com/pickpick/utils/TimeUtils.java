package com.pickpick.utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class TimeUtils {
    private TimeUtils() {
    }

    public static LocalDateTime toLocalDateTime(long unixTime) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(unixTime),
                TimeZone.getDefault().toZoneId()
        );
    }

    public static LocalDateTime toLocalDateTime(String unixTime) {
        return toLocalDateTime(new BigDecimal(unixTime).longValue());
    }
}
