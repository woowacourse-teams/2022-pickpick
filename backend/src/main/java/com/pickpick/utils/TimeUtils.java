package com.pickpick.utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {

    private TimeUtils() {
    }

    public static LocalDateTime toLocalDateTime(final String unixTime) {
        return toLocalDateTime(new BigDecimal(unixTime).longValue());
    }

    private static LocalDateTime toLocalDateTime(final long unixTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.of("Asia/Seoul"));
    }
}
