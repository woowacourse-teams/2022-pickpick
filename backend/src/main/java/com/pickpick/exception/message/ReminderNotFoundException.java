package com.pickpick.exception.message;

import com.pickpick.exception.NotFoundException;

public class ReminderNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "REMINDER_NOT_FOUND";
    private static final String SERVER_MESSAGE = "존재하지 않는 리마인더 조회";
    private static final String CLIENT_MESSAGE = "리마인더를 찾지 못했습니다";

    public ReminderNotFoundException(final Long id) {
        super(String.format("%s -> reminder id: %d", SERVER_MESSAGE, id), CLIENT_MESSAGE, ERROR_CODE);
    }

    public ReminderNotFoundException(final Long messageId, final Long memberId) {
        super(String.format("%s -> message id: %d, member id: %d", SERVER_MESSAGE, messageId, memberId), CLIENT_MESSAGE,
                ERROR_CODE);
    }
}
