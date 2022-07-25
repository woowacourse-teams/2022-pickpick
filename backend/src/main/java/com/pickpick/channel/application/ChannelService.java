package com.pickpick.channel.application;

import com.pickpick.channel.domain.ChannelRepository;
import org.springframework.stereotype.Service;

@Service
public class ChannelService {

    private final ChannelRepository channels;

    public ChannelService(final ChannelRepository channels) {
        this.channels = channels;
    }
}
