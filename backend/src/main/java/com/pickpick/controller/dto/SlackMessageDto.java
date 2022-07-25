package com.pickpick.controller.dto;

import com.pickpick.channel.domain.Channel;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Message;
import com.pickpick.utils.TimeUtils;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SlackMessageDto {

    private String memberSlackId;
    private String slackId;
    private LocalDateTime postedDate;
    private LocalDateTime modifiedDate;
    private String text;
    private String channelSlackId;

    public SlackMessageDto(final String memberSlackId, final String slackId, final String postedDate,
                           final String modifiedDate, final String text, final String channelSlackId) {
        this.memberSlackId = memberSlackId;
        this.slackId = slackId;
        this.postedDate = TimeUtils.toLocalDateTime(postedDate);
        this.modifiedDate = TimeUtils.toLocalDateTime(modifiedDate);
        this.text = text;
        this.channelSlackId = channelSlackId;
    }

    public Message toEntity(final Member member, final Channel channel) {
        return new Message(slackId, text, member, channel, postedDate, modifiedDate);
    }
}
