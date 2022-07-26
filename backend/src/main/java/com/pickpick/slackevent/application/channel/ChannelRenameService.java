package com.pickpick.slackevent.application.channel;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.ChannelNotFoundException;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.channel.dto.SlackChannelRenameDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Transactional
@Service
public class ChannelRenameService implements SlackEventService {

    private static final String CHANNEL = "channel";

    private final ChannelRepository channels;

    public ChannelRenameService(ChannelRepository channels) {
        this.channels = channels;
    }

    @Override
    public void execute(Map<String, Object> requestBody) {
        SlackChannelRenameDto slackChannelRenameDto = convert(requestBody);

        Channel channel = channels.findBySlackId(slackChannelRenameDto.getSlackId())
                .orElseThrow(() -> new ChannelNotFoundException(slackChannelRenameDto.getSlackId()));

        channel.changeName(slackChannelRenameDto.getNewName());
    }

    private SlackChannelRenameDto convert(final Map<String, Object> requestBody) {
        Map<String, String> channel = (Map) requestBody.get(CHANNEL);

        return new SlackChannelRenameDto(
                channel.get("id"),
                channel.get("name")
        );
    }

    @Override
    public boolean isSameSlackEvent(SlackEvent slackEvent) {
        return SlackEvent.CHANNEL_RENAME == slackEvent;
    }
}
