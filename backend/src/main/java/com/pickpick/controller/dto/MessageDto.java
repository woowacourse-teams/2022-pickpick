package com.pickpick.controller.dto;

import com.pickpick.entity.Member;
import com.pickpick.entity.Message;
import com.pickpick.utils.TimeUtils;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MessageDto {

    private String memberSlackId;
    private String slackId;
    private LocalDateTime postedDate;
    private LocalDateTime modifiedDate;
    private String text;

    public MessageDto(final String memberSlackId, final String slackId, final String postedDate,
                      final String modifiedDate, final String text) {
        this.memberSlackId = memberSlackId;
        this.slackId = slackId;
        this.postedDate = TimeUtils.toLocalDateTime(postedDate);
        this.modifiedDate = TimeUtils.toLocalDateTime(modifiedDate);
        this.text = text;
    }

    public Message toEntity(final Member member) {
        return new Message(slackId, text, member, postedDate, modifiedDate);
    }
}
