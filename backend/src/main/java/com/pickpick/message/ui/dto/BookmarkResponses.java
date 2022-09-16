package com.pickpick.message.ui.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class BookmarkResponses {

    private List<BookmarkResponse> bookmarks;

    private boolean hasPast;

    private BookmarkResponses() {
    }

    public BookmarkResponses(final List<BookmarkResponse> bookmarks, final boolean hasPast) {
        this.bookmarks = bookmarks;
        this.hasPast = hasPast;
    }
}
