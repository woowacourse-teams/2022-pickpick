package com.pickpick.message.ui.dto;

import java.util.Objects;
import lombok.Getter;

@Getter
public class ReminderSelectRequest {

    private Long reminderId;
    private int count = 20;

    public ReminderSelectRequest(final Long reminderId, final Integer count) {
        this.reminderId = reminderId;

        if (Objects.nonNull(count)) {
            this.count = count;
        }
    }
}
