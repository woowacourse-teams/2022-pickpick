package com.pickpick.slackevent.application.message;

import com.pickpick.exception.message.MessageNotFoundException;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.message.dto.SlackMessageDto;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageChangedService implements SlackEventService {

    private static final String EVENT = "event";
    private static final String USER = "user";
    private static final String TIMESTAMP = "ts";
    private static final String TEXT = "text";
    private static final String CLIENT_MSG_ID = "client_msg_id";
    private static final String CHANNEL = "channel";
    private static final String MESSAGE = "message";
    private static final String SUBTYPE = "subtype";

    private final MessageThreadBroadcastService messageThreadBroadcastService;
    private final MessageRepository messages;

    public MessageChangedService(final MessageThreadBroadcastService messageThreadBroadcastService,
                                 final MessageRepository messages) {
        this.messageThreadBroadcastService = messageThreadBroadcastService;
        this.messages = messages;
    }

    @Override
    public void execute(final Map<String, Object> requestBody) {
        SlackMessageDto slackMessageDto = convert(requestBody);

        if (isThreadBroadcastEvent(requestBody)) {
            messageThreadBroadcastService.saveWhenSubtypeIsMessageChanged(slackMessageDto);
            return;
        }

        Message message = messages.findBySlackId(slackMessageDto.getSlackId())
                .orElseThrow(() -> new MessageNotFoundException(slackMessageDto.getSlackId()));

        message.changeText(slackMessageDto.getText(), slackMessageDto.getModifiedDate());
    }

    private boolean isThreadBroadcastEvent(final Map<String, Object> requestBody) {
        Map<String, Object> event = (Map) requestBody.get(EVENT);
        Map<String, String> message = (Map) event.get(MESSAGE);
        String subtype = message.get(SUBTYPE);

        return SlackEvent.MESSAGE_THREAD_BROADCAST.isSameSubtype(subtype);
    }

    private SlackMessageDto convert(final Map<String, Object> requestBody) {
        Map<String, Object> event = (Map) requestBody.get(EVENT);
        Map<String, Object> message = (Map) event.get(MESSAGE);

        return new SlackMessageDto(
                (String) message.get(USER),
                (String) message.get(CLIENT_MSG_ID),
                (String) message.get(TIMESTAMP),
                (String) message.get(TIMESTAMP),
                (String) message.get(TEXT),
                (String) event.get(CHANNEL)
        );
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MESSAGE_CHANGED == slackEvent;
    }
}
