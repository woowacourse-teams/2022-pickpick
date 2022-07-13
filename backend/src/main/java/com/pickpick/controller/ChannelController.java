package com.pickpick.controller;

import com.pickpick.controller.dto.ChannelResponse;
import com.pickpick.service.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @GetMapping
    public ResponseEntity<List<ChannelResponse>> getAllChannels() {
        List<ChannelResponse> channelResponses = channelService.findAll()
                .stream()
                .map(ChannelResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(channelResponses);
    }
}
