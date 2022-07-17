package com.pickpick.repository;

import com.pickpick.entity.Channel;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface ChannelRepository extends Repository<Channel, Long> {

    List<Channel> findAll();

    List<Channel> findAllByOrderByName();
}
