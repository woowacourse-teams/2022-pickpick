package com.pickpick.message.application;

import com.pickpick.exception.message.BookmarkDeleteFailureException;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Bookmark;
import com.pickpick.message.domain.BookmarkRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.domain.QBookmark;
import com.pickpick.message.support.SlackIdExtractor;
import com.pickpick.message.ui.dto.BookmarkFindRequest;
import com.pickpick.message.ui.dto.BookmarkRequest;
import com.pickpick.message.ui.dto.BookmarkResponse;
import com.pickpick.message.ui.dto.BookmarkResponses;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class BookmarkService {

    private static final String MENTION_PREFIX = "<@";
    private static final String MENTION_SUFFIX = ">";
    private static final String MENTION_MARK = "@";

    private final BookmarkRepository bookmarks;
    private final MessageRepository messages;
    private final MemberRepository members;
    private final JPAQueryFactory jpaQueryFactory;
    private final SlackIdExtractor slackIdExtractor;

    public BookmarkService(final BookmarkRepository bookmarks, final MessageRepository messages,
                           final MemberRepository members, final JPAQueryFactory jpaQueryFactory,
                           final SlackIdExtractor slackIdExtractor) {
        this.bookmarks = bookmarks;
        this.messages = messages;
        this.members = members;
        this.jpaQueryFactory = jpaQueryFactory;
        this.slackIdExtractor = slackIdExtractor;
    }

    @Transactional
    public void save(final Long memberId, final BookmarkRequest bookmarkRequest) {
        Member member = members.getById(memberId);

        Message message = messages.getById(bookmarkRequest.getMessageId());

        Bookmark bookmark = new Bookmark(member, message);
        bookmarks.save(bookmark);
    }

    public BookmarkResponses find(final BookmarkFindRequest request, final Long memberId) {
        List<Bookmark> bookmarkList = findBookmarks(request, memberId);

        List<BookmarkResponse> responses = toBookmarkResponseList(bookmarkList);
        replaceMentionMembers(memberId, responses);

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

    private void replaceMentionMembers(final Long memberId, final List<BookmarkResponse> bookmarkResponses) {
        Member member = members.getById(memberId);
        List<Member> workspaceMembers = members.findAllByWorkspace(member.getWorkspace());

        Map<String, String> memberNames = workspaceMembers.stream()
                .collect(Collectors.toMap(Member::getSlackId,
                        workspaceMember -> MENTION_MARK + workspaceMember.getUsername()));

        for (BookmarkResponse response : bookmarkResponses) {
            String text = replaceMentionMemberInText(response.getText(), memberNames);
            response.replaceText(text);
        }
    }

    private String replaceMentionMemberInText(String text, final Map<String, String> memberMap) {
        Set<String> slackIds = slackIdExtractor.extract(text);
        for (String slackId : slackIds) {
            String mention = MENTION_PREFIX + slackId + MENTION_SUFFIX;
            text = text.replace(mention, memberMap.getOrDefault(slackId, mention));
        }
        return text;
    }

    @Transactional
    public void delete(final Long messageId, final Long memberId) {
        Bookmark bookmark = bookmarks.findByMessageIdAndMemberId(messageId, memberId)
                .orElseThrow(() -> new BookmarkDeleteFailureException(messageId, memberId));

        bookmarks.deleteById(bookmark.getId());
    }
}
