package com.pickpick.message.ui.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReminderRequest {

    private Long messageId;
    private LocalDateTime reminderDate;

    private ReminderRequest() {
    }

    public ReminderRequest(final Long messageId, final LocalDateTime reminderDate) {
        this.messageId = messageId;
        this.reminderDate = reminderDate;
    }
}
