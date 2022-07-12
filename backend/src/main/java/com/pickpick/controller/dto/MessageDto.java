package com.pickpick.controller.dto;

import com.pickpick.entity.Member;
import com.pickpick.entity.Message;
import com.pickpick.utils.TimeUtils;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MessageDto {
    private String memberSlackId;
    private LocalDateTime postedDate;
    private LocalDateTime modifiedDate;
    private String text;

    public MessageDto(String memberSlackId,
                      String postedDate,
                      String modifiedDate,
                      String text
    ) {
        this.memberSlackId = memberSlackId;
        this.postedDate = TimeUtils.toLocalDateTime(postedDate);
        this.modifiedDate = TimeUtils.toLocalDateTime(modifiedDate);
        this.text = text;
    }

    public Message toEntity(final Member member) {
        return new Message(text, member, postedDate, modifiedDate);
    }
}
