package com.pickpick.slackevent.application.channel.dto;

import lombok.Getter;

@Getter
public class SlackChannelRenameDto {

    private String slackId;
    private String newName;

    private SlackChannelRenameDto() {
    }

    public SlackChannelRenameDto(final String slackId, final String newName) {
        this.slackId = slackId;
        this.newName = newName;
    }
}
