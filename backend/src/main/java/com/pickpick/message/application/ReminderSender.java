package com.pickpick.message.application;

import com.pickpick.message.domain.Reminder;
import com.pickpick.message.domain.ReminderRepository;
import com.pickpick.support.ExternalClient;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        LocalDateTime now = LocalDateTime.now()
                .withSecond(0)
                .withNano(0);
        List<Reminder> foundReminders = reminders.findAllByRemindDate(now);

        for (Reminder reminder : foundReminders) {
            externalClient.sendMessage(reminder);
            reminders.deleteById(reminder.getId());
        }
    }
}
