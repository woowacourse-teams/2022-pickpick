package com.pickpick.slackevent.application.channel;

import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.ChannelNotFoundException;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Transactional
@Service
public class ChannelDeletedService implements SlackEventService {

    private static final String CHANNEL_SLACK_ID = "channel";

    private final ChannelRepository channels;
    private final MessageRepository messages;

    public ChannelDeletedService(ChannelRepository channels, MessageRepository messages) {
        this.channels = channels;
        this.messages = messages;
    }

    @Override
    public void execute(Map<String, Object> requestBody) {
        String channelSlackId = extractChannelSlackId(requestBody);

        channels.findBySlackId(channelSlackId)
                .orElseThrow(() -> new ChannelNotFoundException(channelSlackId));

        messages.deleteAllByChannelSlackId(channelSlackId);
        channels.deleteBySlackId(channelSlackId);
    }

    private String extractChannelSlackId(final Map<String, Object> requestBody) {
        return (String) requestBody.get(CHANNEL_SLACK_ID);
    }

    @Override
    public boolean isSameSlackEvent(SlackEvent slackEvent) {
        return false;
    }
}
