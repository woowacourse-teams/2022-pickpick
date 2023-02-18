package com.pickpick.slackevent.application.message;

import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventHandler;
import com.pickpick.slackevent.application.message.dto.MessageChangedRequest;
import com.pickpick.slackevent.application.message.dto.SlackMessageDto;
import com.pickpick.utils.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageChangedEventHandler implements SlackEventHandler {

    private final MessageThreadBroadcastEventHandler messageThreadBroadcastService;
    private final MessageRepository messages;

    public MessageChangedEventHandler(final MessageThreadBroadcastEventHandler messageThreadBroadcastService,
                                      final MessageRepository messages) {
        this.messageThreadBroadcastService = messageThreadBroadcastService;
        this.messages = messages;
    }

    @Override
    public void execute(final String requestBody) {
        MessageChangedRequest request = JsonUtils.convert(requestBody, MessageChangedRequest.class);
        SlackMessageDto slackMessageDto = request.toDto();

        if (isThreadBroadcastEvent(request)) {
            messageThreadBroadcastService.saveWhenSubtypeIsMessageChanged(slackMessageDto);
            return;
        }

        Message message = messages.getBySlackId(slackMessageDto.getSlackId());

        message.changeText(slackMessageDto.getText(), slackMessageDto.getModifiedDate());
    }

    private boolean isThreadBroadcastEvent(final MessageChangedRequest request) {
        String subtype = request.getEvent().getMessage().getSubtype();
        return SlackEvent.MESSAGE_THREAD_BROADCAST.isSameSubtype(subtype);
    }

    @Override
    public SlackEvent getSlackEvent() {
        return SlackEvent.MESSAGE_CHANGED;
    }
}
