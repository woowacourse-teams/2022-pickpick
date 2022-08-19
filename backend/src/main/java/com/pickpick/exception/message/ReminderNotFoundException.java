package com.pickpick.exception.message;

import com.pickpick.exception.NotFoundException;

public class ReminderNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "리마인더를 찾지 못했습니다";

    public ReminderNotFoundException(final Long id) {
        super(String.format("%s -> reminder id: %d", DEFAULT_MESSAGE, id));
    }

    public ReminderNotFoundException(final Long messageId, final Long memberId) {
        super(String.format("%s -> message id: %d, member id: %d", DEFAULT_MESSAGE, messageId, memberId));
    }
}
