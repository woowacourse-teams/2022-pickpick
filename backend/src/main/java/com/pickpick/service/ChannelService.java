package com.pickpick.service;

import com.pickpick.repository.ChannelRepository;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {

    private final ChannelRepository channels;

    public ChannelService(ChannelRepository channels) {
        this.channels = channels;
    }
}
