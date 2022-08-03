package com.pickpick.message.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface BookmarkRepository extends Repository<Bookmark, Long> {

    void save(Bookmark bookmark);

    Optional<Bookmark> findById(Long id);

    Optional<Bookmark> findByIdAndMemberId(Long id, Long memberId);

    void deleteById(Long id);
}
