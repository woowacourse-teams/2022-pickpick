package com.pickpick.channel.ui;

import com.pickpick.channel.application.ChannelSubscriptionService;
import com.pickpick.channel.ui.dto.ChannelResponses;
import com.pickpick.utils.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
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
    public ChannelResponses findAllChannels(HttpServletRequest request) {
        String memberId = AuthorizationExtractor.extract(request);
        return new ChannelResponses(channelSubscriptionService.findAll(Long.valueOf(memberId)));
    }
}
