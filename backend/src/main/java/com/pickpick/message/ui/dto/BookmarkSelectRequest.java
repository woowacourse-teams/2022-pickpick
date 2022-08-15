package com.pickpick.message.ui.dto;

import java.util.Objects;
import lombok.Getter;

@Getter
public class BookmarkSelectRequest {

    private Long bookmarkId;
    private int count = 20;

    public BookmarkSelectRequest(final Long bookmarkId, final Integer count) {
        this.bookmarkId = bookmarkId;

        if (Objects.nonNull(count)) {
            this.count = count;
        }
    }
}
