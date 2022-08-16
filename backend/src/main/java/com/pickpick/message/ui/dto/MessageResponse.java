package com.pickpick.message.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageResponse {

    private Long id;
    private Long memberId;
    private String username;
    private String userThumbnail;
    private String text;
    private LocalDateTime postedDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime remindDate;

    @JsonProperty(value = "isBookmarked")
    private boolean bookmarked;

    @JsonProperty(value = "isSetReminded")
    private boolean setReminded;

    private MessageResponse() {
    }

    @Builder
    public MessageResponse(final Long id,
                           final Long memberId,
                           final String username,
                           final String userThumbnail,
                           final String text,
                           final LocalDateTime postedDate,
                           final LocalDateTime modifiedDate,
                           final boolean isBookmarked,
                           final boolean isSetReminded,
                           final LocalDateTime remindDate) {
        this.id = id;
        this.memberId = memberId;
        this.username = username;
        this.userThumbnail = userThumbnail;
        this.text = text;
        this.postedDate = postedDate;
        this.modifiedDate = modifiedDate;
        this.bookmarked = isBookmarked;
        this.setReminded = isSetReminded;
        this.remindDate = remindDate;
    }

    @QueryProjection
    public MessageResponse(final Long id,
                           final Long memberId,
                           final String username,
                           final String userThumbnail,
                           final String text,
                           final LocalDateTime postedDate,
                           final LocalDateTime modifiedDate,
                           final Long bookmarkId,
                           final Long reminderId,
                           final LocalDateTime remindDate) {
        this.id = id;
        this.memberId = memberId;
        this.username = username;
        this.userThumbnail = userThumbnail;
        this.text = text;
        this.postedDate = postedDate;
        this.modifiedDate = modifiedDate;
        this.bookmarked = Objects.nonNull(bookmarkId);
        this.setReminded = Objects.nonNull(reminderId);
        this.remindDate = remindDate;
    }
}
