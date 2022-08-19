package com.pickpick.message.ui.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReminderSaveRequest {

    private Long messageId;
    private LocalDateTime reminderDate;

    private ReminderSaveRequest() {
    }

    public ReminderSaveRequest(final Long messageId, final LocalDateTime reminderDate) {
        this.messageId = messageId;
        this.reminderDate = reminderDate;
    }
}
