package com.pickpick.message.ui.dto;

import lombok.Getter;

@Getter
public class BookmarkRequest {

    private Long messageId;

    private BookmarkRequest() {
    }

    public BookmarkRequest(Long messageId) {
        this.messageId = messageId;
    }
}
