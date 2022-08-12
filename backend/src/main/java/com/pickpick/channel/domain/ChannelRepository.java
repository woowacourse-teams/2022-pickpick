package com.pickpick.channel.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ChannelRepository extends Repository<Channel, Long> {

    Channel save(Channel channel);

    List<Channel> findAll();

    List<Channel> findAllByOrderByName();

    Optional<Channel> findBySlackId(String slackId);

    Optional<Channel> findById(Long id);

    void deleteBySlackId(String slackId);
}
