package com.pickpick.slackevent.application.channel.dto;

import com.pickpick.channel.domain.Channel;
import com.pickpick.workspace.domain.Workspace;
import lombok.Getter;

@Getter
public class ChannelRequest {

    private ChannelEventDto event;
    private String teamId;

    private ChannelRequest() {
    }

    public ChannelRequest(final ChannelEventDto event, final String teamId) {
        this.event = event;
        this.teamId = teamId;
    }

    public SlackChannelRenameDto toDto() {
        ChannelDto channel = event.getChannel();

        return new SlackChannelRenameDto(
                channel.getId(),
                channel.getName()
        );
    }

    public Channel toEntity(final Workspace workspace) {
        ChannelDto channel = event.getChannel();
        return new Channel(
                channel.getId(),
                channel.getName(),
                workspace
        );
    }
}
