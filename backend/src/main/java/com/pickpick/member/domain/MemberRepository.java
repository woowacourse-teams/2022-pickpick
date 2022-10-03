package com.pickpick.member.domain;

import com.pickpick.exception.member.MemberNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member member);

    void saveAll(Iterable<Member> members);

    Optional<Member> findById(Long id);

    Optional<Member> findBySlackId(String slackId);

    List<Member> findAll();
    
    default Member getById(final Long id) {
        return findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    default Member getBySlackId(final String slackId) {
        return findBySlackId(slackId)
                .orElseThrow(() -> new MemberNotFoundException(slackId));
    }
}
