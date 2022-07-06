package com.pickpick.utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {
    private TimeUtils() {
    }

    public static LocalDateTime toLocalDateTime(String unixTime) {
        return toLocalDateTime(new BigDecimal(unixTime).longValue());
    }

    private static LocalDateTime toLocalDateTime(long unixTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault());
    }
}
