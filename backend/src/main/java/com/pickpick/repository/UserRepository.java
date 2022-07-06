package com.pickpick.repository;

import com.pickpick.entity.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    Optional<User> findBySlackId(String slackId);
}
