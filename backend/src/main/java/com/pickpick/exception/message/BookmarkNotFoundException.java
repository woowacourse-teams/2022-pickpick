package com.pickpick.exception.message;

import com.pickpick.exception.NotFoundException;

public class BookmarkNotFoundException extends NotFoundException {

    private static final String ERROR_CODE = "BOOKMARK_NOT_FOUND";
    private static final String SERVER_MESSAGE = "존재하지 않는 북마크 조회";
    private static final String CLIENT_MESSAGE = "북마크를 찾지 못했습니다.";

    public BookmarkNotFoundException(final Long id) {
        super(String.format("%s -> bookmark id: %d", SERVER_MESSAGE, id), CLIENT_MESSAGE, ERROR_CODE);
    }
}
