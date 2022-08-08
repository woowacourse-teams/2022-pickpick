package com.pickpick.exception.message;

import com.pickpick.exception.NotFoundException;

public class BookmarkNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "북마크를 찾지 못했습니다";

    public BookmarkNotFoundException(final Long id) {
        super(String.format("%s -> bookmark id: %d", DEFAULT_MESSAGE, id));
    }
}
