package com.pickpick.message.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BookmarkResponses {

    private List<BookmarkResponse> bookmarks;

    @JsonProperty(value = "hasPast")
    private boolean hasPast;

    private BookmarkResponses() {
    }

    public BookmarkResponses(final List<BookmarkResponse> bookmarks, final boolean hasPast) {
        this.bookmarks = bookmarks;
        this.hasPast = hasPast;
    }
}
