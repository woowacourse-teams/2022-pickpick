package com.pickpick.service;

import com.pickpick.entity.Channel;
import com.pickpick.repository.ChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {

    private final ChannelRepository channels;

    public ChannelService(ChannelRepository channels) {
        this.channels = channels;
    }

    public List<Channel> findAll() {
        return channels.findAll();
    }
}
