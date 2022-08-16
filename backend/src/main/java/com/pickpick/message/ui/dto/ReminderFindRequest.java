package com.pickpick.message.ui.dto;

import java.util.Objects;
import lombok.Getter;

@Getter
public class ReminderFindRequest {

    private Long reminderId;
    private int count = 20;

    public ReminderFindRequest(final Long reminderId, final Integer count) {
        this.reminderId = reminderId;

        if (Objects.nonNull(count)) {
            this.count = count;
        }
    }
}
