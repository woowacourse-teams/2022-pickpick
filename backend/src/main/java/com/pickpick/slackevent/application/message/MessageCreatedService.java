package com.pickpick.slackevent.application.message;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.MemberNotFoundException;
import com.pickpick.exception.SlackClientException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.message.dto.SlackMessageDto;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import java.io.IOException;
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
    private final MethodsClient slackClient;

    public MessageCreatedService(final MessageRepository messages, final MemberRepository members,
                                 final ChannelRepository channels, final MethodsClient slackClient) {
        this.messages = messages;
        this.members = members;
        this.channels = channels;
        this.slackClient = slackClient;
    }

    @Override
    public void execute(final Map<String, Object> requestBody) {
        SlackMessageDto slackMessageDto = convert(requestBody);

        String memberSlackId = slackMessageDto.getMemberSlackId();
        Member member = members.findBySlackId(memberSlackId)
                .orElseThrow(() -> new MemberNotFoundException(memberSlackId));

        String channelSlackId = slackMessageDto.getChannelSlackId();

        Channel channel = channels.findBySlackId(channelSlackId)
                .orElseGet(() -> createChannel(channelSlackId));

        messages.save(slackMessageDto.toEntity(member, channel));
    }

    private Channel createChannel(final String channelSlackId) {
        try {
            Conversation conversation = slackClient.conversationsInfo(
                    request -> request.channel(channelSlackId)
            ).getChannel();

            Channel channel = toChannel(conversation);
            channels.save(channel);

            return channel;
        } catch (IOException | SlackApiException e) {
            throw new SlackClientException(e);
        }
    }

    private Channel toChannel(final Conversation channel) {
        return new Channel(channel.getId(), channel.getName());
    }

    private SlackMessageDto convert(final Map<String, Object> requestBody) {
        final Map<String, Object> event = (Map<String, Object>) requestBody.get(EVENT);

        return new SlackMessageDto(
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
