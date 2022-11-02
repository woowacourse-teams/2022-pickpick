package com.pickpick.message.ui.dto;

import com.pickpick.message.domain.Bookmark;
import com.pickpick.message.domain.Message;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookmarkResponse {

    private Long id;
    private Long messageId;
    private Long memberId;
    private String username;
    private String userThumbnail;
    private String text;
    private LocalDateTime postedDate;
    private LocalDateTime modifiedDate;

    private BookmarkResponse() {
    }

    public BookmarkResponse(final Long id,
                            final Long messageId,
                            final Long memberId,
                            final String username,
                            final String userThumbnail,
                            final String text,
                            final LocalDateTime postedDate,
                            final LocalDateTime modifiedDate) {
        this.id = id;
        this.messageId = messageId;
        this.memberId = memberId;
        this.username = username;
        this.userThumbnail = userThumbnail;
        this.text = text;
        this.postedDate = postedDate;
        this.modifiedDate = modifiedDate;
    }

    public static BookmarkResponse from(final Bookmark bookmark) {
        Message message = bookmark.getMessage();

        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .messageId(message.getId())
                .memberId(message.getMember().getId())
                .username(message.getMember().getUsername())
                .userThumbnail(message.getMember().getThumbnailUrl())
                .text(message.getText())
                .postedDate(message.getPostedDate())
                .modifiedDate(message.getModifiedDate())
                .build();
    }
}
