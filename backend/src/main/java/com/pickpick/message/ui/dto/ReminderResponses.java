package com.pickpick.message.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ReminderResponses implements MessageTextResponses<ReminderResponse> {

    private List<ReminderResponse> reminders;

    @JsonProperty(value = "hasFuture")
    private boolean hasFuture;

    private ReminderResponses() {
    }

    public ReminderResponses(final List<ReminderResponse> reminders, final boolean hasFuture) {
        this.reminders = reminders;
        this.hasFuture = hasFuture;
    }

    public List<ReminderResponse> getReminders() {
        return reminders;
    }

    public boolean hasFuture() {
        return hasFuture;
    }

    @Override
    public List<ReminderResponse> findContents() {
        return reminders;
    }
}
