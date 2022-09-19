package com.pickpick.slackevent.application.message;

import com.pickpick.channel.application.ChannelCreateService;
import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.member.MemberNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.message.dto.MessageCreatedEventDto;
import com.pickpick.slackevent.application.message.dto.MessageCreatedRequest;
import com.pickpick.slackevent.application.message.dto.SlackMessageDto;
import com.pickpick.utils.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageThreadBroadcastService implements SlackEventService {

    private final MessageRepository messages;
    private final MemberRepository members;
    private final ChannelRepository channels;
    private final ChannelCreateService channelCreateService;

    public MessageThreadBroadcastService(final MessageRepository messages, final MemberRepository members,
                                         final ChannelRepository channels, final ChannelCreateService channelCreateService) {
        this.messages = messages;
        this.members = members;
        this.channels = channels;
        this.channelCreateService = channelCreateService;
    }

    @Override
    public void execute(final String requestBody) {
        SlackMessageDto slackMessageDto = convert(requestBody);

        save(slackMessageDto);
    }

    private SlackMessageDto convert(final String requestBody) {
        MessageCreatedRequest request = JsonUtils.convert(requestBody, MessageCreatedRequest.class);
        MessageCreatedEventDto message = request.getEvent();

        return new SlackMessageDto(
                message.getUser(),
                message.getClientMsgId(),
                message.getTs(),
                message.getTs(),
                message.getText(),
                message.getChannel()
        );
    }

    public void saveWhenSubtypeIsMessageChanged(final SlackMessageDto slackMessageDto) {
        messages.findBySlackId(slackMessageDto.getSlackId())
                .ifPresentOrElse(
                        message -> message.changeText(slackMessageDto.getText(), slackMessageDto.getModifiedDate()),
                        () -> save(slackMessageDto)
                );
    }

    private void save(final SlackMessageDto slackMessageDto) {
        String memberSlackId = slackMessageDto.getMemberSlackId();
        Member member = members.findBySlackId(memberSlackId)
                .orElseThrow(() -> new MemberNotFoundException(memberSlackId));

        String channelSlackId = slackMessageDto.getChannelSlackId();

        Channel channel = channels.findBySlackId(channelSlackId)
                .orElseGet(() -> channelCreateService.createChannel(channelSlackId));

        messages.save(slackMessageDto.toEntity(member, channel));
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.MESSAGE_THREAD_BROADCAST == slackEvent;
    }
}
