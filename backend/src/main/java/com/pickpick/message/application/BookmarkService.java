package com.pickpick.message.application;

import com.pickpick.exception.member.MemberNotFoundException;
import com.pickpick.exception.message.BookmarkDeleteFailureException;
import com.pickpick.exception.message.BookmarkNotFoundException;
import com.pickpick.exception.message.MessageNotFoundException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Bookmark;
import com.pickpick.message.domain.BookmarkRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.domain.QBookmark;
import com.pickpick.message.ui.dto.BookmarkFindRequest;
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

    public BookmarkResponses find(final BookmarkFindRequest request, final Long memberId) {
        List<Bookmark> bookmarkList = findBookmarks(request, memberId);

        return new BookmarkResponses(toBookmarkResponseList(bookmarkList), hasPast(bookmarkList, memberId));
    }

    private List<Bookmark> findBookmarks(final BookmarkFindRequest request, final Long memberId) {
        return jpaQueryFactory
                .selectFrom(QBookmark.bookmark)
                .leftJoin(QBookmark.bookmark.message)
                .fetchJoin()
                .leftJoin(QBookmark.bookmark.member)
                .fetchJoin()
                .where(QBookmark.bookmark.member.id.eq(memberId))
                .where(bookmarkIdCondition(request.getBookmarkId()))
                .orderBy(QBookmark.bookmark.message.postedDate.desc())
                .limit(request.getCount())
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

    private boolean hasPast(final List<Bookmark> bookmarkList, final Long memberId) {
        if (bookmarkList.isEmpty()) {
            return false;
        }

        Integer result = jpaQueryFactory
                .selectOne()
                .from(QBookmark.bookmark)
                .where(QBookmark.bookmark.member.id.eq(memberId))
                .where(meetHasPastCondition(bookmarkList))
                .fetchFirst();

        return result != null;
    }

    private BooleanExpression meetHasPastCondition(final List<Bookmark> bookmarkList) {
        Bookmark targetBookmark = bookmarkList.get(bookmarkList.size() - 1);

        LocalDateTime messageDate = targetBookmark.getMessage().getPostedDate();

        return QBookmark.bookmark.message.postedDate.before(messageDate);
    }

    @Transactional
    public void delete(final Long messageId, final Long memberId) {
        Bookmark bookmark = bookmarks.findByMessageIdAndMemberId(messageId, memberId)
                .orElseThrow(() -> new BookmarkDeleteFailureException(messageId, memberId));

        bookmarks.deleteById(bookmark.getId());
    }
}
