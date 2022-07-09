package com.pickpick.controller;

import com.pickpick.controller.dto.ChannelResponses;
import com.pickpick.service.ChannelSubscriptionService;
import com.pickpick.utils.AuthorizationExtractor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/channels")
public class ChannelController{

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
