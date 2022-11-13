package com.pickpick.message.application;

import com.pickpick.message.domain.Reminder;
import com.pickpick.message.domain.ReminderRepository;
import com.pickpick.support.ExternalClient;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ReminderSender {

    private final Clock clock;
    private final ReminderRepository reminders;
    private final ExternalClient externalClient;

    public ReminderSender(final Clock clock, final ReminderRepository reminders, final ExternalClient externalClient) {
        this.clock = clock;
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

        reminders.deleteInBatch(foundReminders);
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock)
                .withSecond(0)
                .withNano(0);
    }
}
