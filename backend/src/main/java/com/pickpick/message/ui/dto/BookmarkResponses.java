package com.pickpick.message.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BookmarkResponses {

    private List<BookmarkResponse> bookmarks;

    @JsonProperty(value = "isLast")
    private boolean last;

    private BookmarkResponses() {
    }

    public BookmarkResponses(final List<BookmarkResponse> bookmarks, final boolean last) {
        this.bookmarks = bookmarks;
        this.last = last;
    }
}
