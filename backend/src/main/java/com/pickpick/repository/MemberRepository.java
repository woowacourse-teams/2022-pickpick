package com.pickpick.repository;

import com.pickpick.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Member findById(Long id);

    Optional<Member> findBySlackId(String slackId);

    void save(Member member);

    void saveAll(Iterable<Member> members);

    List<Member> findAll();

    void deleteAll();
}
