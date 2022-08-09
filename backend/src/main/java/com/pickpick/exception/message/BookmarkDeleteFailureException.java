package com.pickpick.exception.message;

import com.pickpick.exception.BadRequestException;

public class BookmarkDeleteFailureException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "외래키가 일치하는 북마크가 없어 삭제 목적 조회 실패";
    private static final String CLIENT_MESSAGE = "해당 북마크를 삭제할 수 없습니다.";

    public BookmarkDeleteFailureException(final Long id, final Long memberId) {
        super(String.format("%s -> bookmark message id: %d, member id: %d", DEFAULT_MESSAGE, id, memberId));
    }

    @Override
    public String getClientMessage() {
        return CLIENT_MESSAGE;
    }
}
