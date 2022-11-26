package com.pickpick.slackevent.application.message;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventHandler;
import com.pickpick.slackevent.application.message.dto.MessageCreatedRequest;
import com.pickpick.slackevent.application.message.dto.SlackMessageDto;
import com.pickpick.utils.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageFileShareEventHandler implements SlackEventHandler {

    private final MessageRepository messages;
    private final MemberRepository members;
    private final ChannelRepository channels;

    public MessageFileShareEventHandler(final MessageRepository messages, final MemberRepository members,
                                        final ChannelRepository channels) {
        this.messages = messages;
        this.members = members;
        this.channels = channels;
    }

    @Override
    public void execute(final String requestBody) {
        SlackMessageDto slackMessageDto = convert(requestBody);

        Member member = findMember(slackMessageDto);
        Channel channel = findChannel(slackMessageDto);

        messages.save(slackMessageDto.toEntity(member, channel));
    }

    private SlackMessageDto convert(final String requestBody) {
        MessageCreatedRequest request = JsonUtils.convert(requestBody, MessageCreatedRequest.class);
        return request.toDto();
    }

    private Member findMember(final SlackMessageDto slackMessageDto) {
        String memberSlackId = slackMessageDto.getMemberSlackId();

        return members.getBySlackId(memberSlackId);
    }

    private Channel findChannel(final SlackMessageDto slackMessageDto) {
        String channelSlackId = slackMessageDto.getChannelSlackId();
        return channels.getBySlackId(channelSlackId);
    }

    @Override
    public SlackEvent getSlackEvent() {
        return SlackEvent.MESSAGE_FILE_SHARE;
    }
}
