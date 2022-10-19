package com.pickpick.channel.ui;

import com.pickpick.auth.support.AuthenticationPrincipal;
import com.pickpick.channel.application.ChannelService;
import com.pickpick.channel.ui.dto.ChannelResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(final ChannelService channelService) {
        this.channelService = channelService;
    }

    @GetMapping
    public ChannelResponses findAllChannels(final @AuthenticationPrincipal Long memberId) {
        return channelService.findByWorkspace(memberId);
    }
}
