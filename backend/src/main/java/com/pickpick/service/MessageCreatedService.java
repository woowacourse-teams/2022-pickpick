package com.pickpick.service;

import com.pickpick.controller.dto.MessageDto;
import com.pickpick.controller.event.SlackEvent;
import com.pickpick.entity.Member;
import com.pickpick.exception.MemberNotFoundException;
import com.pickpick.repository.MemberRepository;
import com.pickpick.repository.MessageRepository;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageCreatedService implements SlackEventService {

    private static final String EVENT = "event";
    private static final String USER = "user";
    private static final String TIMESTAMP = "ts";
    private static final String TEXT = "text";

    private final MessageRepository messages;
    private final MemberRepository members;

    public MessageCreatedService(final MessageRepository messages, final MemberRepository members) {
        this.messages = messages;
        this.members = members;
    }

    @Override
    public void execute(final Map<String, Object> requestBody) {
        MessageDto messageDto = convert(requestBody);

        Member member = members.findBySlackId(messageDto.getMemberSlackId())
                .orElseThrow(MemberNotFoundException::new);

        messages.save(messageDto.toEntity(member));
    }

    private MessageDto convert(final Map<String, Object> requestBody) {
        final Map<String, Object> event = (Map<String, Object>) requestBody.get(EVENT);

        return new MessageDto(
                (String) event.get(USER),
                (String) event.get(TIMESTAMP),
                (String) event.get(TIMESTAMP),
                (String) event.get(TEXT)
        );
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MESSAGE_CREATED == slackEvent;
    }
}
