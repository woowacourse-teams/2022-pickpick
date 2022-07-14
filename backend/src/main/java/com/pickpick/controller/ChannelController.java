package com.pickpick.controller;

import com.pickpick.controller.dto.ChannelResponse;
import com.pickpick.controller.dto.ChannelResponses;
import com.pickpick.service.ChannelService;
import com.pickpick.service.ChannelSubscriptionService;
import com.pickpick.utils.AuthorizationExtractor;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
public class ChannelController{

    private final ChannelService channelService;
    private final ChannelSubscriptionService channelSubscriptionService;

    public ChannelController(ChannelService channelService,
                             ChannelSubscriptionService channelSubscriptionService) {
        this.channelService = channelService;
        this.channelSubscriptionService = channelSubscriptionService;
    }

    @GetMapping
    public ChannelResponses getAllChannels(HttpServletRequest request) {
        String memberId = AuthorizationExtractor.extract(request);
        return new ChannelResponses(channelSubscriptionService.findAll(Long.valueOf(memberId))
                .stream()
                .map(ChannelResponse::from)
                .collect(Collectors.toList()));
    }
}
