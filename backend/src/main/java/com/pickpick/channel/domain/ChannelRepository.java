package com.pickpick.channel.domain;

import com.pickpick.exception.channel.ChannelNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ChannelRepository extends Repository<Channel, Long> {

    Channel save(Channel channel);

    List<Channel> findAll();

    List<Channel> findAllByOrderByName();

    Optional<Channel> findById(Long id);

    Optional<Channel> findBySlackId(String slackId);

    void deleteBySlackId(String slackId);

    default Channel getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id));
    }

    default Channel getBySlackId(final String slackId) {
        return findBySlackId(slackId)
                .orElseThrow(() -> new ChannelNotFoundException(slackId));
    }
}
