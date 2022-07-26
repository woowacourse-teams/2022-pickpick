package com.pickpick.channel.ui;

import com.pickpick.channel.application.ChannelSubscriptionService;
import com.pickpick.channel.ui.dto.ChannelOrderRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionRequest;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponse;
import com.pickpick.channel.ui.dto.ChannelSubscriptionResponses;
import com.pickpick.utils.AuthorizationExtractor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/channel-subscription")
public class ChannelSubscriptionController {

    private final ChannelSubscriptionService channelSubscriptionService;

    public ChannelSubscriptionController(final ChannelSubscriptionService channelSubscriptionService) {
        this.channelSubscriptionService = channelSubscriptionService;
    }

    @GetMapping
    public ChannelSubscriptionResponses getAllChannels(HttpServletRequest request) {
        String memberId = AuthorizationExtractor.extract(request);
        return new ChannelSubscriptionResponses(
                channelSubscriptionService.findAllOrderByViewOrder(Long.parseLong(memberId))
                        .stream()
                        .map(ChannelSubscriptionResponse::from)
                        .collect(Collectors.toList()));
    }

    @PostMapping
    public void subscribeChannel(HttpServletRequest request,
                                 @RequestBody ChannelSubscriptionRequest subscriptionRequest) {
        String memberId = AuthorizationExtractor.extract(request);
        channelSubscriptionService.save(subscriptionRequest, Long.parseLong(memberId));
    }

    @PutMapping
    public void updateViewOrders(HttpServletRequest request, @RequestBody List<ChannelOrderRequest> orderRequests) {
        String memberId = AuthorizationExtractor.extract(request);
        channelSubscriptionService.updateOrders(orderRequests, Long.parseLong(memberId));
    }

    @DeleteMapping
    public void unsubscribeChannel(HttpServletRequest request, @RequestParam Long channelId) {
        String memberId = AuthorizationExtractor.extract(request);
        channelSubscriptionService.delete(channelId, Long.parseLong(memberId));
    }
}
