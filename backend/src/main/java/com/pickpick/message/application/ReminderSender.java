package com.pickpick.message.application;

import com.pickpick.message.domain.Reminder;
import com.pickpick.message.domain.ReminderRepository;
import com.pickpick.support.ExternalClient;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ReminderSender {

    private final ReminderRepository reminders;
    private final ExternalClient externalClient;

    public ReminderSender(final ReminderRepository reminders, final ExternalClient externalClient) {
        this.reminders = reminders;
        this.externalClient = externalClient;
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void sendRemindMessage() {
        LocalDateTime nowTime = now();

        List<Reminder> foundReminders = reminders.findAllByRemindDate(nowTime);
        for (Reminder reminder : foundReminders) {
            externalClient.sendMessage(reminder);
        }

        reminders.deleteAllByRemindDate(nowTime);
    }

    private LocalDateTime now() {
        return LocalDateTime.now()
                .withSecond(0)
                .withNano(0);
    }
}
