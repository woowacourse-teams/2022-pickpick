package com.pickpick.slackevent.application.channel;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.channel.ChannelNotFoundException;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.channel.dto.SlackChannelRenameDto;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class ChannelRenameService implements SlackEventService {

    private static final String CHANNEL = "channel";
    private static final String ID = "id";
    private static final String NAME = "name";

    private final ChannelRepository channels;

    public ChannelRenameService(ChannelRepository channels) {
        this.channels = channels;
    }

    @Override
    public void execute(final Map<String, Object> requestBody) {
        SlackChannelRenameDto slackChannelRenameDto = convert(requestBody);

        Channel channel = channels.findBySlackId(slackChannelRenameDto.getSlackId())
                .orElseThrow(() -> new ChannelNotFoundException(slackChannelRenameDto.getSlackId()));

        channel.changeName(slackChannelRenameDto.getNewName());
    }

    private SlackChannelRenameDto convert(final Map<String, Object> requestBody) {
        Map<String, String> channel = (Map) requestBody.get(CHANNEL);

        return new SlackChannelRenameDto(
                channel.get(ID),
                channel.get(NAME)
        );
    }

    @Override
    public boolean isSameSlackEvent(final SlackEvent slackEvent) {
        return SlackEvent.CHANNEL_RENAME == slackEvent;
    }
}
