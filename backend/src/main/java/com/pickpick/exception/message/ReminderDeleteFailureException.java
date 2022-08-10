package com.pickpick.exception.message;

import com.pickpick.exception.BadRequestException;

public class ReminderDeleteFailureException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "외래키가 일치하는 리마인더가 없어 삭제 목적 조회 실패";
    private static final String CLIENT_MESSAGE = "해당 리마인더를 삭제할 수 없습니다.";

    public ReminderDeleteFailureException(final Long messageId, final Long memberId) {
        super(String.format("%s -> reminder message id: %d, member id: %d", DEFAULT_MESSAGE, messageId, memberId));
    }

    @Override
    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
