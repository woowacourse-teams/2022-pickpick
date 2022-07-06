package com.pickpick.controller.dto;

import com.pickpick.entity.Message;
import com.pickpick.entity.User;
import com.pickpick.utils.TimeUtils;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDto {
    private String userSlackId;
    private LocalDateTime postedDate;
    private LocalDateTime modifiedDate;
    private String text;

    public MessageDto(String userSlackId,
                      String postedDate,
                      String modifiedDate,
                      String text
    ) {
        this.userSlackId = userSlackId;
        this.postedDate = TimeUtils.toLocalDateTime(postedDate);
        this.modifiedDate = TimeUtils.toLocalDateTime(modifiedDate);
        this.text = text;
    }

    public Message toEntity(final User user) {
        return new Message(text, user, postedDate, modifiedDate);
    }
}
