package com.pickpick.slackevent.application.channel;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.channel.dto.ChannelRequest;
import com.pickpick.slackevent.application.channel.dto.SlackChannelRenameDto;
import com.pickpick.utils.JsonUtils;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class ChannelRenameService implements SlackEventService {

    private final ChannelRepository channels;

    public ChannelRenameService(ChannelRepository channels) {
        this.channels = channels;
    }

    @Override
    public void execute(final String requestBody) {
        SlackChannelRenameDto slackChannelRenameDto = convert(requestBody);

        Channel channel = channels.getBySlackId(slackChannelRenameDto.getSlackId());

        channel.changeName(slackChannelRenameDto.getNewName());
    }

    private SlackChannelRenameDto convert(final String requestBody) {
        ChannelRequest request = JsonUtils.convert(requestBody, ChannelRequest.class);
        return request.toDto();
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.CHANNEL_RENAME == slackEvent;
    }
}
