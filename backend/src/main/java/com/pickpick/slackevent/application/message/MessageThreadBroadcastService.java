package com.pickpick.slackevent.application.message;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MessageThreadBroadcastService implements SlackEventService {

    private final MessageRepository messages;
    private final MemberRepository members;
    private final ChannelRepository channels;

    public MessageThreadBroadcastService(final MessageRepository messages, final MemberRepository members,
                                         final ChannelRepository channels) {
        this.messages = messages;
        this.members = members;
        this.channels = channels;
    }

    @Override
    public void execute(final String requestBody) {
        SlackMessageDto slackMessageDto = convert(requestBody);

        save(slackMessageDto);
    }

    private SlackMessageDto convert(final String requestBody) {
        MessageCreatedRequest request = JsonUtils.convert(requestBody, MessageCreatedRequest.class);
        return request.toDto();
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
        Member member = members.getBySlackId(memberSlackId);

        String channelSlackId = slackMessageDto.getChannelSlackId();
        Channel channel = channels.getBySlackId(channelSlackId);

        messages.save(slackMessageDto.toEntity(member, channel));
    }

    @Override
    public SlackEvent getSlackEvent() {
        return SlackEvent.MESSAGE_THREAD_BROADCAST;
    }
}
