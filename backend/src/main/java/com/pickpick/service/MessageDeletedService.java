package com.pickpick.service;

import com.pickpick.controller.event.SlackEvent;
import com.pickpick.message.domain.MessageRepository;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageDeletedService implements SlackEventService {

    private static final String EVENT = "event";
    private static final String PREVIOUS_MESSAGE = "previous_message";
    private static final String CLIENT_MSG_ID = "client_msg_id";

    private final MessageRepository messages;

    public MessageDeletedService(final MessageRepository messages) {
        this.messages = messages;
    }

    @Override
    public void execute(final Map<String, Object> requestBody) {
        String slackId = extractMessageSlackId(requestBody);

        messages.deleteBySlackId(slackId);
    }

    private String extractMessageSlackId(final Map<String, Object> requestBody) {
        Map<String, Object> event = (Map<String, Object>) requestBody.get(EVENT);
        Map<String, String> previousMessage = (Map<String, String>) event.get(PREVIOUS_MESSAGE);

        return previousMessage.get(CLIENT_MSG_ID);
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MESSAGE_DELETED == slackEvent;
    }
}
