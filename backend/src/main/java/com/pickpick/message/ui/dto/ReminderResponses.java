package com.pickpick.message.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class ReminderResponses {

    private List<ReminderResponse> reminders;

    @JsonProperty(value = "isLast")
    private boolean last;

    private ReminderResponses() {
    }

    public ReminderResponses(final List<ReminderResponse> reminders, final boolean last) {
        this.reminders = reminders;
        this.last = last;
    }
}
