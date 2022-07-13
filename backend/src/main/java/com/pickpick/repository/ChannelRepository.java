package com.pickpick.repository;

import com.pickpick.entity.Channel;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ChannelRepository extends Repository<Channel, Long> {

    List<Channel> findAll();
}
