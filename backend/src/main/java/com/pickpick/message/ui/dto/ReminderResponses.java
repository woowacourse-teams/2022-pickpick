package com.pickpick.message.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class ReminderResponses {

    private List<ReminderResponse> reminders;

    @JsonProperty(value = "hasFuture")
    private boolean hasFuture;

    private ReminderResponses() {
    }

    public ReminderResponses(final List<ReminderResponse> reminders, final boolean hasFuture) {
        this.reminders = reminders;
        this.hasFuture = hasFuture;
    }
}
