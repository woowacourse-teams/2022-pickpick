package com.pickpick.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

@Setter
@Getter
public class SlackMessageRequest {

    @Nullable
    private String keyword;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @NotNull
    private List<Long> channelIds;

    private boolean needPastMessage = true;

    @Nullable
    private Long messageId;

    @Min(0)
    private int messageCount = 20;

    public SlackMessageRequest() {
    }
}
