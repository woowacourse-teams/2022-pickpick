package com.pickpick.member.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findById(Long id);

    Optional<Member> findBySlackId(String slackId);

    Member save(Member member);

    void saveAll(Iterable<Member> members);

    List<Member> findAll();

    void deleteAll();
}
