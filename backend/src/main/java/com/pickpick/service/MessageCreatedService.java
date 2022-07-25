package com.pickpick.service;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.controller.dto.MessageDto;
import com.pickpick.controller.event.SlackEvent;
import com.pickpick.entity.Member;
import com.pickpick.exception.ChannelNotFoundException;
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
    private static final String CLIENT_MSG_ID = "client_msg_id";
    private static final String CHANNEL = "channel";

    private final MessageRepository messages;
    private final MemberRepository members;
    private final ChannelRepository channels;

    public MessageCreatedService(final MessageRepository messages, final MemberRepository members,
                                 final ChannelRepository channels) {
        this.messages = messages;
        this.members = members;
        this.channels = channels;
    }

    @Override
    public void execute(final Map<String, Object> requestBody) {
        MessageDto messageDto = convert(requestBody);

        String memberSlackId = messageDto.getMemberSlackId();
        Member member = members.findBySlackId(memberSlackId)
                .orElseThrow(() -> new MemberNotFoundException(memberSlackId));

        String channelSlackId = messageDto.getChannelSlackId();
        Channel channel = channels.findBySlackId(channelSlackId)
                .orElseThrow(() -> new ChannelNotFoundException(channelSlackId));

        messages.save(messageDto.toEntity(member, channel));
    }

    private MessageDto convert(final Map<String, Object> requestBody) {
        final Map<String, Object> event = (Map<String, Object>) requestBody.get(EVENT);

        return new MessageDto(
                (String) event.get(USER),
                (String) event.get(CLIENT_MSG_ID),
                (String) event.get(TIMESTAMP),
                (String) event.get(TIMESTAMP),
                (String) event.get(TEXT),
                (String) event.get(CHANNEL)
        );
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MESSAGE_CREATED == slackEvent;
    }
}
