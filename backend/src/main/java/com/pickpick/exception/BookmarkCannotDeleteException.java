package com.pickpick.exception;

public class BookmarkCannotDeleteException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "해당 북마크를 삭제할 수 없습니다";

    public BookmarkCannotDeleteException(final Long id, final Long memberId) {
        super(String.format("%s -> bookmark id: %d, member id: %d", DEFAULT_MESSAGE, id, memberId));
    }
}
