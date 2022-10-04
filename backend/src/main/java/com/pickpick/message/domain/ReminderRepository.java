package com.pickpick.message.domain;

import com.pickpick.exception.message.ReminderNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface ReminderRepository extends Repository<Reminder, Long> {

    Reminder save(Reminder reminder);

    Optional<Reminder> findById(Long id);

    @Query("select r from Reminder r WHERE r.message.id = :messageId and r.member.id = :memberId")
    Optional<Reminder> findByMessageIdAndMemberId(Long messageId, Long memberId);

    List<Reminder> findAllByRemindDate(LocalDateTime remindDate);

    void deleteById(Long id);

    default Reminder getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new ReminderNotFoundException(id));
    }

    default Reminder getByMessageIdAndMemberId(Long messageId, Long memberId) {
        return findByMessageIdAndMemberId(messageId, memberId)
                .orElseThrow(() -> new ReminderNotFoundException(messageId, memberId));
    }
}
