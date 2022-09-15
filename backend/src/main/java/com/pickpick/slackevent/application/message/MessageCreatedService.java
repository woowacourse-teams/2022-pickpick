package com.pickpick.slackevent.application.message;

import com.pickpick.channel.application.ChannelService;
import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.member.MemberNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.message.dto.MessageCreatedDto;
import com.pickpick.slackevent.application.message.dto.MessageCreatedRequest;
import com.pickpick.slackevent.application.message.dto.SlackMessageDto;
import com.pickpick.utils.JsonUtils;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageCreatedService implements SlackEventService {

    private final MessageRepository messages;
    private final MemberRepository members;
    private final ChannelRepository channels;
    private final ChannelService channelService;

    public MessageCreatedService(final MessageRepository messages, final MemberRepository members,
                                 final ChannelRepository channels, final ChannelService channelService) {
        this.messages = messages;
        this.members = members;
        this.channels = channels;
        this.channelService = channelService;
    }

    @Override
    public void execute(final String requestBody) {
        if (isReplyEvent(requestBody)) {
            return;
        }

        SlackMessageDto slackMessageDto = convert(requestBody);

        String memberSlackId = slackMessageDto.getMemberSlackId();
        Member member = members.findBySlackId(memberSlackId)
                .orElseThrow(() -> new MemberNotFoundException(memberSlackId));

        String channelSlackId = slackMessageDto.getChannelSlackId();

        Channel channel = channels.findBySlackId(channelSlackId)
                .orElseGet(() -> channelService.createChannel(channelSlackId));

        messages.save(slackMessageDto.toEntity(member, channel));
    }

    private boolean isReplyEvent(final String requestBody) {
        MessageCreatedRequest request = JsonUtils.convert(requestBody, MessageCreatedRequest.class);
        return Objects.nonNull(request.getEvent().getThreadTs());
    }

    private SlackMessageDto convert(final String requestBody) {
        MessageCreatedRequest request = JsonUtils.convert(requestBody, MessageCreatedRequest.class);
        MessageCreatedDto message = request.getEvent();

        return new SlackMessageDto(
                message.getUser(),
                message.getClientMsgId(),
                message.getTs(),
                message.getTs(),
                message.getText(),
                message.getChannel()
        );
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MESSAGE_CREATED == slackEvent;
    }
}
