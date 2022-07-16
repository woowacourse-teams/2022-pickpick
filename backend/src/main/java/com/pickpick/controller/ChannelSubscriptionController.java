package com.pickpick.controller;

import com.pickpick.controller.dto.ChannelOrderRequest;
import com.pickpick.controller.dto.ChannelSubscriptionResponse;
import com.pickpick.controller.dto.ChannelSubscriptionResponses;
import com.pickpick.service.ChannelSubscriptionService;
import com.pickpick.utils.AuthorizationExtractor;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channel-subscription")
public class ChannelSubscriptionController {

    private final ChannelSubscriptionService channelSubscriptionService;

    public ChannelSubscriptionController(ChannelSubscriptionService channelSubscriptionService) {
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

    @PutMapping
    public void updateViewOrders(HttpServletRequest request, @RequestBody List<ChannelOrderRequest> orderRequests) {
        String memberId = AuthorizationExtractor.extract(request);
        channelSubscriptionService.updateOrders(orderRequests, Long.parseLong(memberId));
    }
}
