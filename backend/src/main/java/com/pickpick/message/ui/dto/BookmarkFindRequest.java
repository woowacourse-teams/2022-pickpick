package com.pickpick.message.ui.dto;

import java.util.Objects;
import lombok.Getter;

@Getter
public class BookmarkFindRequest {

    private Long bookmarkId;
    private int count = 20;

    public BookmarkFindRequest(final Long bookmarkId, final Integer count) {
        this.bookmarkId = bookmarkId;

        if (Objects.nonNull(count)) {
            this.count = count;
        }
    }
}
