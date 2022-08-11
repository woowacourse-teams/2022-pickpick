package com.pickpick.message.application;

import com.pickpick.exception.message.SlackSendMessageFailureException;
import com.pickpick.message.domain.Reminder;
import com.pickpick.message.domain.ReminderRepository;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import java.io.IOException;
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

    private static final String REMINDER_TEXT_FORMAT = "리마인드 메시지가 도착했습니다!\uD83D\uDC39 \n> %s";
    private static final String ERROR_TEXT = "";

    private final ReminderRepository reminders;
    private final MethodsClient slackClient;

    public ReminderSender(final ReminderRepository reminders, final MethodsClient slackClient) {
        this.reminders = reminders;
        this.slackClient = slackClient;
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void sendRemindMessage() {
        LocalDateTime now = LocalDateTime.now()
                .withSecond(0)
                .withNano(0);
        List<Reminder> foundReminders = reminders.findAllByRemindDate(now);

        for (Reminder reminder : foundReminders) {
            try {
                sendMessage(reminder);
            } catch (IOException | SlackApiException | SlackSendMessageFailureException e) {
                log.error(ERROR_TEXT, e);
            } finally {
                reminders.deleteById(reminder.getId());
            }
        }
    }

    private void sendMessage(final Reminder reminder)
            throws IOException, SlackApiException, SlackSendMessageFailureException {

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(reminder.getMember().getSlackId())
                .text(String.format(REMINDER_TEXT_FORMAT, reminder.getMessage().getText()))
                .build();

        ChatPostMessageResponse response = slackClient.chatPostMessage(request);
        if (!response.isOk()) {
            throw new SlackSendMessageFailureException(response.getError());
        }
    }
}
