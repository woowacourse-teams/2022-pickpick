package com.pickpick.message.application;

import com.pickpick.exception.message.BookmarkDeleteFailureException;
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
import com.pickpick.support.MentionIdReplaceable;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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
        Member member = members.getById(memberId);

        Message message = messages.getById(bookmarkRequest.getMessageId());

        Bookmark bookmark = new Bookmark(member, message);
        bookmarks.save(bookmark);
    }

    @MentionIdReplaceable
    public BookmarkResponses find(final BookmarkFindRequest request, final Long memberId) {
        List<Bookmark> bookmarkList = findBookmarks(request, memberId);

        List<BookmarkResponse> responses = toBookmarkResponseList(bookmarkList);

        return new BookmarkResponses(responses, hasPast(bookmarkList, memberId));
    }

    private List<Bookmark> findBookmarks(final BookmarkFindRequest request, final Long memberId) {
        return jpaQueryFactory
                .selectFrom(QBookmark.bookmark)
                .leftJoin(QBookmark.bookmark.message)
                .fetchJoin()
                .leftJoin(QBookmark.bookmark.member)
                .fetchJoin()
                .where(QBookmark.bookmark.member.id.eq(memberId))
                .where(bookmarkCreatedDateCondition(request.getBookmarkId()))
                .orderBy(QBookmark.bookmark.createdDate.desc())
                .limit(request.getCount())
                .fetch();
    }

    private List<BookmarkResponse> toBookmarkResponseList(final List<Bookmark> foundBookmarks) {
        return foundBookmarks.stream()
                .map(BookmarkResponse::from)
                .collect(Collectors.toList());
    }

    private BooleanExpression bookmarkCreatedDateCondition(final Long bookmarkId) {
        if (bookmarkId == null) {
            return null;
        }

        Bookmark bookmark = bookmarks.getById(bookmarkId);

        return QBookmark.bookmark.createdDate.before(bookmark.getCreatedDate());
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
        return QBookmark.bookmark.createdDate.before(targetBookmark.getCreatedDate());
    }

    @Transactional
    public void delete(final Long messageId, final Long memberId) {
        Bookmark bookmark = bookmarks.findByMessageIdAndMemberId(messageId, memberId)
                .orElseThrow(() -> new BookmarkDeleteFailureException(messageId, memberId));

        bookmarks.deleteById(bookmark.getId());
    }
}
