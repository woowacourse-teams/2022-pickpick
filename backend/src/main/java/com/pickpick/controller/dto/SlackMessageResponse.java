package com.pickpick.controller.dto;

import com.pickpick.entity.Member;
import com.pickpick.entity.Message;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SlackMessageResponse {

    private Long id;
    private Long memberId;
    private String username;
    private String userThumbnail;
    private String text;
    private LocalDateTime postedDate;
    private LocalDateTime modifiedDate;

    private SlackMessageResponse() {
    }

    public SlackMessageResponse(final Long id,
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

    public static SlackMessageResponse from(final Message message) {
        final Member member = message.getMember();

        return new SlackMessageResponse(
                message.getId(),
                member.getId(),
                member.getUsername(),
                member.getThumbnailUrl(),
                message.getText(),
                message.getPostedDate(),
                message.getModifiedDate()
        );
    }
}
