package com.pickpick.message.ui.dto;

import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.Reminder;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReminderResponse {

    private Long id;
    private Long messageId;
    private String username;
    private String userThumbnail;
    private String text;
    private LocalDateTime postedDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime remindDate;

    private ReminderResponse() {
    }

    @Builder
    public ReminderResponse(final Long id, final Long messageId, final String username, final String userThumbnail,
                            final String text, final LocalDateTime postedDate, final LocalDateTime modifiedDate,
                            final LocalDateTime remindDate) {
        this.id = id;
        this.messageId = messageId;
        this.username = username;
        this.userThumbnail = userThumbnail;
        this.text = text;
        this.postedDate = postedDate;
        this.modifiedDate = modifiedDate;
        this.remindDate = remindDate;
    }

    public static ReminderResponse from(final Reminder reminder) {
        Message message = reminder.getMessage();
        Member member = message.getMember();

        return ReminderResponse.builder()
                .id(reminder.getId())
                .messageId(message.getId())
                .username(member.getUsername())
                .userThumbnail(member.getThumbnailUrl())
                .text(message.getText())
                .postedDate(message.getPostedDate())
                .modifiedDate(message.getModifiedDate())
                .remindDate(reminder.getRemindDate())
                .build();
    }
}
