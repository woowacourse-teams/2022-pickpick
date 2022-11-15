package com.pickpick.slackevent.application.channel;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.slackevent.application.SlackEvent;
import com.pickpick.slackevent.application.SlackEventService;
import com.pickpick.slackevent.application.channel.dto.ChannelRequest;
import com.pickpick.utils.JsonUtils;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class ChannelCreatedService implements SlackEventService {

    private final WorkspaceRepository workspaces;
    private final ChannelRepository channels;

    public ChannelCreatedService(final WorkspaceRepository workspaces, final ChannelRepository channels) {
        this.workspaces = workspaces;
        this.channels = channels;
    }

    @Override
    public void execute(final String requestBody) {
        ChannelRequest request = convert(requestBody);
        Workspace workspace = workspaces.getBySlackId(request.getTeamId());
        Channel channel = request.toEntity(workspace);
        channels.save(channel);
    }

    private ChannelRequest convert(final String requestBody) {
        return JsonUtils.convert(requestBody, ChannelRequest.class);
    }

    @Override
    public SlackEvent getSlackEvent() {
        return SlackEvent.CHANNEL_CREATED;
    }
}
