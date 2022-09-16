package com.pickpick.slackevent.application.channel.dto;

import lombok.Getter;

@Getter
public class ChannelRenameRequest {

    private ChannelDto channel;

    private ChannelRenameRequest() {
    }

    public ChannelRenameRequest(final ChannelDto channel) {
        this.channel = channel;
    }

    public SlackChannelRenameDto toDto() {
        return new SlackChannelRenameDto(
                channel.getId(),
                channel.getName()
        );
    }
}
