package com.pickpick.fixture;

import com.pickpick.message.ui.dto.BookmarkFindRequest;

public class BookmarkFindRequestFactory {

    public static BookmarkFindRequest emptyQueryParams() {
        return new BookmarkFindRequest(null, null);
    }

    public static BookmarkFindRequest onlyCount(final int count) {
        return new BookmarkFindRequest(null, count);
    }

    public static BookmarkFindRequest onlyBookmarkId(final Long id) {
        return new BookmarkFindRequest(id, null);
    }

    public static BookmarkFindRequest withBookmarkIdAndCount(final Long id, final int count) {
        return new BookmarkFindRequest(id, count);
    }
}
