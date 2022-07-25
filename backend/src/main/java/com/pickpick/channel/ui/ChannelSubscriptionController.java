package com.pickpick.channel.ui;

import com.pickpick.auth.support.AuthenticationPrincipal;
import com.pickpick.channel.application.ChannelSubscriptionService;
import com.pickpick.channel.ui.dto.ChannelOrderRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponse;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponses;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channel-subscription")
public class ChannelSubscriptionController {

    private final ChannelSubscriptionService channelSubscriptionService;

    public ChannelSubscriptionController(final ChannelSubscriptionService channelSubscriptionService) {
        this.channelSubscriptionService = channelSubscriptionService;
    }

    @GetMapping
    public ChannelSubscriptionResponses getAllChannels(final @AuthenticationPrincipal Long memberId) {
        return new ChannelSubscriptionResponses(
                channelSubscriptionService.findAllOrderByViewOrder(memberId)
                        .stream()
                        .map(ChannelSubscriptionResponse::from)
                        .collect(Collectors.toList()));
    }

    @PostMapping
    public void subscribeChannel(final @AuthenticationPrincipal Long memberId,
                                 final @RequestBody ChannelSubscriptionRequest subscriptionRequest) {
        channelSubscriptionService.save(subscriptionRequest, memberId);
    }

    @PutMapping
    public void updateViewOrders(final @AuthenticationPrincipal Long memberId,
                                 final @RequestBody List<ChannelOrderRequest> orderRequests) {
        channelSubscriptionService.updateOrders(orderRequests, memberId);
    }

    @DeleteMapping
    public void unsubscribeChannel(final @AuthenticationPrincipal Long memberId,
                                   final @RequestParam Long channelId) {
        channelSubscriptionService.delete(channelId, memberId);
    }
}
