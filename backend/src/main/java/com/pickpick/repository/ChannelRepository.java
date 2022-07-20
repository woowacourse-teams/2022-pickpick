package com.pickpick.repository;

import com.pickpick.entity.Channel;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends Repository<Channel, Long> {

    void save(Channel channel);

    List<Channel> findAll();

    List<Channel> findAllByOrderByName();

    Optional<Channel> findBySlackId(String slackId);

    Optional<Channel> findById(Long id);

    void saveAll(Iterable<Channel> channels);
}
