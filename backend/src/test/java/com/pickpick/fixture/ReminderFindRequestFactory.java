package com.pickpick.fixture;

import com.pickpick.message.ui.dto.ReminderFindRequest;

public class ReminderFindRequestFactory {

    public static ReminderFindRequest emptyQueryParams() {
        return new ReminderFindRequest(null, null);
    }

    public static ReminderFindRequest onlyCount(final int count) {
        return new ReminderFindRequest(null, count);
    }

    public static ReminderFindRequest onlyReminderId(final Long id) {
        return new ReminderFindRequest(id, null);
    }

    public static ReminderFindRequest reminderIdAndCount(final Long id, final int count) {
        return new ReminderFindRequest(id, count);
    }
}
