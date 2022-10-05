package com.pickpick.slackevent.application.message;

import com.pickpick.channel.application.ChannelCreateService;
import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
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
    private final ChannelCreateService channelCreateService;

    public MessageCreatedService(final MessageRepository messages, final MemberRepository members,
                                 final ChannelRepository channels, final ChannelCreateService channelCreateService) {
        this.messages = messages;
        this.members = members;
        this.channels = channels;
        this.channelCreateService = channelCreateService;
    }

    @Override
    public void execute(final String requestBody) {
        MessageCreatedRequest request = JsonUtils.convert(requestBody, MessageCreatedRequest.class);
        if (isReplyEvent(request)) {
            return;
        }

        SlackMessageDto slackMessageDto = request.toDto();

        String memberSlackId = slackMessageDto.getMemberSlackId();
        Member member = members.getBySlackId(memberSlackId);

        String channelSlackId = slackMessageDto.getChannelSlackId();

        Channel channel = channels.findBySlackId(channelSlackId)
                .orElseGet(() -> channelCreateService.createChannel(channelSlackId, member.getWorkspace()));

        messages.save(slackMessageDto.toEntity(member, channel));
    }

    private boolean isReplyEvent(final MessageCreatedRequest request) {
        return Objects.nonNull(request.getEvent().getThreadTs());
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MESSAGE_CREATED == slackEvent;
    }
}
