package com.pickpick.message.domain;

import org.springframework.data.repository.Repository;

public interface BookmarkRepository extends Repository<Bookmark, Long> {
    void save(Bookmark bookmark);
}
