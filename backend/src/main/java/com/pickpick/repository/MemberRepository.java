package com.pickpick.repository;

import com.pickpick.entity.Member;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {
    Optional<Member> findBySlackId(String slackId);

    void saveAll(Iterable<Member> members);

    List<Member> findAll();

    void deleteAll();
}
