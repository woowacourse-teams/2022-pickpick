package com.pickpick.channel.ui;

import com.pickpick.auth.support.AuthenticationPrincipal;
import com.pickpick.channel.application.ChannelSubscriptionService;
import com.pickpick.channel.ui.dto.ChannelResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelSubscriptionService channelSubscriptionService;

    public ChannelController(final ChannelSubscriptionService channelSubscriptionService) {
        this.channelSubscriptionService = channelSubscriptionService;
    }

    @GetMapping
    public ChannelResponses findAllChannels(final @AuthenticationPrincipal Long memberId) {
        return new ChannelResponses(channelSubscriptionService.findAll(memberId));
    }
}
