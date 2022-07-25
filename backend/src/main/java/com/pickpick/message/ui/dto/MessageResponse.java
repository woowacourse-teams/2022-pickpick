package com.pickpick.message.ui.dto;

import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Message;
import java.time.LocalDateTime;
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

    private MessageResponse() {
    }

    public MessageResponse(final Long id,
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

    public static MessageResponse from(final Message message) {
        final Member member = message.getMember();

        return new MessageResponse(
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
