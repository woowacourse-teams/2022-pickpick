package com.pickpick.channel.domain;

import com.pickpick.exception.channel.ChannelNotFoundException;
import com.pickpick.workspace.domain.Workspace;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ChannelRepository extends Repository<Channel, Long> {

    Channel save(Channel channel);

    void saveAll(Iterable<Channel> channels);

    List<Channel> findAll();

    List<Channel> findAllByWorkspaceOrderByName(Workspace workspace);

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
