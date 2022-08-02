package com.pickpick.message.ui.dto;

import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Bookmark;
import com.pickpick.message.domain.Message;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookmarkResponse {

    private Long id;
    private Long memberId;
    private String username;
    private String userThumbnail;
    private String text;
    private LocalDateTime postedDate;
    private LocalDateTime modifiedDate;

    private BookmarkResponse() {
    }

    public BookmarkResponse(final Long id,
                            final Long memberId,
                            final String username,
                            final String userThumbnail,
                            final String text,
                            final LocalDateTime postedDate,
                            final LocalDateTime modifiedDate) {
        this.id = id;
        this.memberId = memberId;
        this.username = username;
        this.userThumbnail = userThumbnail;
        this.text = text;
        this.postedDate = postedDate;
        this.modifiedDate = modifiedDate;
    }

    public static BookmarkResponse from(final Bookmark bookmark) {
        Member member = bookmark.getMember();
        Message message = bookmark.getMessage();

        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .memberId(member.getId())
                .username(member.getUsername())
                .userThumbnail(member.getThumbnailUrl())
                .text(message.getText())
                .postedDate(message.getPostedDate())
                .modifiedDate(message.getModifiedDate())
                .build();
    }
}
