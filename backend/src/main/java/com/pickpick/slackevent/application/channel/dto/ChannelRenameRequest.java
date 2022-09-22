package com.pickpick.slackevent.application.channel.dto;

import lombok.Getter;

@Getter
public class ChannelRenameRequest {

    private ChannelEventDto event;

    private ChannelRenameRequest() {
    }

    public ChannelRenameRequest(final ChannelEventDto event) {
        this.event = event;
    }

    public SlackChannelRenameDto toDto() {
        ChannelDto channel = event.getChannel();
        
        return new SlackChannelRenameDto(
                channel.getId(),
                channel.getName()
        );
    }
}
