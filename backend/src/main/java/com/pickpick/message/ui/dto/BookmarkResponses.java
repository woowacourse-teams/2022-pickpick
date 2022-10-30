package com.pickpick.message.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class BookmarkResponses implements MessageTextResponses<BookmarkResponse> {

    private List<BookmarkResponse> bookmarks;

    @JsonProperty(value = "hasPast")
    private boolean hasPast;

    private BookmarkResponses() {
    }

    public BookmarkResponses(final List<BookmarkResponse> bookmarks, final boolean hasPast) {
        this.bookmarks = bookmarks;
        this.hasPast = hasPast;
    }

    public List<BookmarkResponse> getBookmarks() {
        return bookmarks;
    }

    public boolean hasPast() {
        return hasPast;
    }

    @Override
    public List<BookmarkResponse> findContents() {
        return bookmarks;
    }
}
