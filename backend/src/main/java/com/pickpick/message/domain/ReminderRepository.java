package com.pickpick.message.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ReminderRepository extends Repository<Reminder, Long> {

    Optional<Reminder> findById(Long id);
}
