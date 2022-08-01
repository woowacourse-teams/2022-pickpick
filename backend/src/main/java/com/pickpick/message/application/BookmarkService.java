package com.pickpick.message.application;

import com.pickpick.exception.MemberNotFoundException;
import com.pickpick.exception.MessageNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Bookmark;
import com.pickpick.message.domain.BookmarkRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.ui.dto.BookmarkRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class BookmarkService {
    private final BookmarkRepository bookmarks;
    private final MessageRepository messages;
    private final MemberRepository members;

    public BookmarkService(BookmarkRepository bookmarks, MessageRepository messages, MemberRepository members) {
        this.bookmarks = bookmarks;
        this.messages = messages;
        this.members = members;
    }

    @Transactional
    public void save(Long memberId, BookmarkRequest bookmarkRequest) {
        Member member = members.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Message message = messages.findById(bookmarkRequest.getMessageId())
                .orElseThrow(() -> new MessageNotFoundException(bookmarkRequest.getMessageId()));

        Bookmark bookmark = new Bookmark(member, message);
        bookmarks.save(bookmark);
    }
}
