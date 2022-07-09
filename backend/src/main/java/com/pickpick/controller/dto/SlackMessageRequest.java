package com.pickpick.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class SlackMessageRequest {

    private String keyword;
    private LocalDateTime date;
    private List<Long> channelIds;
    private boolean needPastMessage = true;
    private Long messageId;
    private int messageCount = 20;

    public SlackMessageRequest(final String keyword, final String date, final List<Long> channelIds,
                               final Boolean needPastMessage, final Long messageId, final Integer messageCount) {
        if (StringUtils.hasText(keyword)) {
            this.keyword = keyword;
        }

        if (StringUtils.hasText(date)) {
            this.date = LocalDateTime.parse(date);
        }

        if (Objects.nonNull(channelIds)) {
            this.channelIds = channelIds;
        }

        if (Objects.nonNull(needPastMessage)) {
            this.needPastMessage = needPastMessage;
        }

        if (Objects.nonNull(messageId)) {
            this.messageId = messageId;
        }

        if (Objects.nonNull(messageCount)) {
            this.messageCount = messageCount;
        }
    }
}
