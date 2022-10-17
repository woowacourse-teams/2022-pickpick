package com.pickpick.exception.message;

import com.pickpick.exception.BadRequestException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReminderInvalidDateException extends BadRequestException {

    private static final String ERROR_CODE = "REMINDER_INVALID_REMIND_DATE";
    private static final String SERVER_MESSAGE = "유효하지 않은 리마인더 시각";
    private static final String CLIENT_MESSAGE = "리마인더 시각은 현재보다 과거로 설정할 수 없습니다.";

    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ReminderInvalidDateException(final LocalDateTime remindDate) {
        super(String.format("%s -> 현재 시각: %s, 리마인드 시각: %s",
                        SERVER_MESSAGE, LocalDateTime.now().format(pattern), remindDate.format(pattern)),
                CLIENT_MESSAGE, ERROR_CODE);
    }
}
