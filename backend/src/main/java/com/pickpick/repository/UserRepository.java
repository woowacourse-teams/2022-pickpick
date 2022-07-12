package com.pickpick.repository;

import com.pickpick.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, Long> {
    Optional<User> findBySlackId(String slackId);

    void saveAll(Iterable<User> users);

    List<User> findAll();
}
