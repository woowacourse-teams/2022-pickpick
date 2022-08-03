package com.pickpick.message.application;

import com.pickpick.exception.BookmarkDeleteFailureException;
import com.pickpick.exception.BookmarkNotFoundException;
import com.pickpick.exception.MemberNotFoundException;
import com.pickpick.exception.MessageNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Bookmark;
import com.pickpick.message.domain.BookmarkRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.domain.QBookmark;
import com.pickpick.message.ui.dto.BookmarkRequest;
import com.pickpick.message.ui.dto.BookmarkResponse;
import com.pickpick.message.ui.dto.BookmarkResponses;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class BookmarkService {

    public static final int COUNT = 20;
    
    private final BookmarkRepository bookmarks;
    private final MessageRepository messages;
    private final MemberRepository members;
    private final JPAQueryFactory jpaQueryFactory;

    public BookmarkService(final BookmarkRepository bookmarks, final MessageRepository messages,
                           final MemberRepository members, final JPAQueryFactory jpaQueryFactory) {
        this.bookmarks = bookmarks;
        this.messages = messages;
        this.members = members;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public void save(final Long memberId, final BookmarkRequest bookmarkRequest) {
        Member member = members.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Message message = messages.findById(bookmarkRequest.getMessageId())
                .orElseThrow(() -> new MessageNotFoundException(bookmarkRequest.getMessageId()));

        Bookmark bookmark = new Bookmark(member, message);
        bookmarks.save(bookmark);
    }

    public BookmarkResponses find(final Long bookmarkId, final Long memberId) {
        List<Bookmark> bookmarkList = findBookmarks(bookmarkId, memberId);

        return new BookmarkResponses(toBookmarkResponseList(bookmarkList), isLast(bookmarkList, memberId));
    }

    private List<Bookmark> findBookmarks(final Long bookmarkId, final Long memberId) {
        return jpaQueryFactory
                .selectFrom(QBookmark.bookmark)
                .leftJoin(QBookmark.bookmark.message)
                .fetchJoin()
                .leftJoin(QBookmark.bookmark.member)
                .fetchJoin()
                .where(QBookmark.bookmark.member.id.eq(memberId))
                .where(bookmarkIdCondition(bookmarkId))
                .orderBy(QBookmark.bookmark.message.postedDate.desc())
                .limit(COUNT)
                .fetch();
    }

    private List<BookmarkResponse> toBookmarkResponseList(final List<Bookmark> foundBookmarks) {
        return foundBookmarks.stream()
                .map(BookmarkResponse::from)
                .collect(Collectors.toList());
    }

    private BooleanExpression bookmarkIdCondition(final Long bookmarkId) {
        if (Objects.isNull(bookmarkId)) {
            return null;
        }

        Bookmark bookmark = bookmarks.findById(bookmarkId)
                .orElseThrow(() -> new BookmarkNotFoundException(bookmarkId));

        LocalDateTime messageDate = bookmark.getMessage().getPostedDate();

        return QBookmark.bookmark.message.postedDate.before(messageDate);
    }

    private boolean isLast(final List<Bookmark> bookmarkList, final Long memberId) {
        if (bookmarkList.isEmpty()) {
            return true;
        }

        Integer result = jpaQueryFactory
                .selectOne()
                .from(QBookmark.bookmark)
                .where(QBookmark.bookmark.member.id.eq(memberId))
                .where(meetIsLastCondition(bookmarkList))
                .fetchFirst();

        return Objects.isNull(result);
    }

    private BooleanExpression meetIsLastCondition(final List<Bookmark> bookmarkList) {
        Bookmark targetBookmark = bookmarkList.get(bookmarkList.size() - 1);

        LocalDateTime messageDate = targetBookmark.getMessage().getPostedDate();

        return QBookmark.bookmark.message.postedDate.before(messageDate);
    }

    @Transactional
    public void delete(final Long bookmarkId, final Long memberId) {
        bookmarks.findByIdAndMemberId(bookmarkId, memberId)
                .orElseThrow(() -> new BookmarkDeleteFailureException(bookmarkId, memberId));

        bookmarks.deleteById(bookmarkId);
    }
}
