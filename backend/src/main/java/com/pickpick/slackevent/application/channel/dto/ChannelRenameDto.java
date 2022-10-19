package com.pickpick.slackevent.application.channel.dto;

import lombok.Getter;

@Getter
public class ChannelRenameDto {

    private String slackId;
    private String newName;

    private ChannelRenameDto() {
    }

    public ChannelRenameDto(final String slackId, final String newName) {
        this.slackId = slackId;
        this.newName = newName;
    }
}
