package com.pickpick.repository;

import com.pickpick.entity.Channel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ChannelRepository extends Repository<Channel, Long> {

    Optional<Channel> findById(Long id);

    List<Channel> findAll();

    List<Channel> findAllByOrderByName();
}
