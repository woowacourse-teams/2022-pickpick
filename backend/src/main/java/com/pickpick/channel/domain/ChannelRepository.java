package com.pickpick.channel.domain;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends Repository<Channel, Long> {

    void save(Channel channel);

    List<Channel> findAll();

    List<Channel> findAllByOrderByName();

    Optional<Channel> findBySlackId(String slackId);

    Optional<Channel> findById(Long id);

    void deleteBySlackId(String slackId);
}
