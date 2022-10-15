package com.pickpick.message.application;

import static com.pickpick.fixture.ChannelFixture.NOTICE;
import static com.pickpick.fixture.MemberFixture.HOPE;
import static com.pickpick.fixture.MemberFixture.KKOJAE;
import static com.pickpick.fixture.MessageFixtures.PLAIN_20220712_18_00_00;
import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.exception.message.BookmarkDeleteFailureException;
import com.pickpick.fixture.BookmarkFindRequestFactory;
import com.pickpick.fixture.MemberFixture;
import com.pickpick.fixture.MessageFixtures;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Bookmark;
import com.pickpick.message.domain.BookmarkRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.ui.dto.BookmarkFindRequest;
import com.pickpick.message.ui.dto.BookmarkRequest;
import com.pickpick.message.ui.dto.BookmarkResponse;
import com.pickpick.message.ui.dto.BookmarkResponses;
import com.pickpick.support.DatabaseCleaner;
import com.pickpick.workspace.domain.Workspace;
import com.pickpick.workspace.domain.WorkspaceRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.DateTimeProvider;

@SpringBootTest
class BookmarkServiceTest {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private MemberRepository members;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private BookmarkRepository bookmarks;

    @Autowired
    private WorkspaceRepository workspaces;

    @MockBean
    private DateTimeProvider dateTimeProvider;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @DisplayName("북마크 생성/삭제")
    @Nested
    class saveAndDelete {

        @AfterEach
        void tearDown() {
            databaseCleaner.clear();
        }

        @DisplayName("북마크 생성")
        @Test
        void save() {
            // given
            Workspace workspace = workspaces.save(JUPJUP.create());
            Member member = members.save(HOPE.create(workspace));
            Channel channel = channels.save(NOTICE.create());
            Message message = messages.save(PLAIN_20220712_18_00_00.create(channel, member));

            BookmarkRequest bookmarkRequest = new BookmarkRequest(message.getId());
            int beforeSaveSize = findBookmarksSize(member);

            // when
            bookmarkService.save(member.getId(), bookmarkRequest);

            // then
            int afterSaveSize = findBookmarksSize(member);
            assertThat(beforeSaveSize + 1).isEqualTo(afterSaveSize);
        }

        @DisplayName("북마크 삭제")
        @Test
        void delete() {
            // given
            Workspace workspace = workspaces.save(JUPJUP.create());
            Member member = members.save(HOPE.create(workspace));
            Channel channel = channels.save(NOTICE.create());
            Message message = messages.save(PLAIN_20220712_18_00_00.create(channel, member));

            Bookmark bookmark = bookmarks.save(new Bookmark(member, message));

            // when
            bookmarkService.delete(message.getId(), member.getId());

            // then
            Optional<Bookmark> actual = bookmarks.findById(bookmark.getId());
            assertThat(actual).isEmpty();
        }

        @DisplayName("다른 사용자의 북마크 삭제시 예외")
        @Test
        void deleteOtherMembers() {
            // given
            Workspace workspace = workspaces.save(JUPJUP.create());
            Member owner = members.save(HOPE.create(workspace));
            Member other = members.save(KKOJAE.create(workspace));
            Channel channel = channels.save(NOTICE.create());
            Message message = messages.save(PLAIN_20220712_18_00_00.create(channel, other));

            Bookmark bookmark = new Bookmark(owner, message);
            bookmarks.save(bookmark);

            // when & then
            assertThatThrownBy(() -> bookmarkService.delete(bookmark.getId(), other.getId()))
                    .isInstanceOf(BookmarkDeleteFailureException.class);
        }

        private int findBookmarksSize(final Member member) {
            return bookmarkService.find(BookmarkFindRequestFactory.emptyQueryParams(), member.getId())
                    .getBookmarks()
                    .size();
        }
    }

    @DisplayName("북마크 조회")
    @TestInstance(Lifecycle.PER_CLASS)
    @Nested
    class find {

        Workspace workspace = workspaces.save(JUPJUP.create());
        Member hope = saveMember(HOPE, workspace);
        Member kkojae = saveMember(KKOJAE, workspace);

        Channel notice = channels.save(NOTICE.create());

        List<Message> savedMessages = createAndSaveMessages(notice, kkojae);
        List<Bookmark> hopesBookmarks = saveBookmarksAtDifferentTimes(hope, savedMessages);
        List<Bookmark> kkojaesBookmarks = saveBookmarksAtDifferentTimes(kkojae, savedMessages);

        @AfterAll
        void tearDown() {
            databaseCleaner.clear();
        }

        @DisplayName("해당 멤버의 북마크만 조회된다")
        @Test
        void membersOwnBookmarks() {
            int totalSize = hopesBookmarks.size() + kkojaesBookmarks.size();
            List<BookmarkResponse> foundHopesBookmarks = bookmarkService.find(
                            BookmarkFindRequestFactory.onlyCount(totalSize), hope.getId())
                    .getBookmarks();

            List<Long> hopesBookmarkIds = extractIds(hopesBookmarks);
            List<Long> kkojaesBookamrkIds = extractIds(kkojaesBookmarks);

            assertAll(
                    () -> assertThat(foundHopesBookmarks).extracting("id").containsAll(hopesBookmarkIds),
                    () -> assertThat(foundHopesBookmarks).extracting("id")
                            .doesNotContainAnyElementsOf(kkojaesBookamrkIds)
            );
        }

        @DisplayName("더 조회 할 북마크가 없다면 hasPast는 false다")
        @Test
        void noMoreBookmarksToFindHasPastFalse() {
            int totalSize = hopesBookmarks.size() + kkojaesBookmarks.size();
            BookmarkResponses response = bookmarkService.find(BookmarkFindRequestFactory.onlyCount(totalSize),
                    hope.getId());

            assertThat(response.hasPast()).isFalse();
        }

        @DisplayName("조회 할 북마크가 남아있다면 hasPast는 True다")
        @Test
        void moreBookmarksToFindHasPastTrue() {
            int lessThanTotal = kkojaesBookmarks.size() - 1;
            BookmarkResponses response = bookmarkService.find(BookmarkFindRequestFactory.onlyCount(lessThanTotal),
                    hope.getId());

            assertThat(response.hasPast()).isTrue();
        }

        @DisplayName("정렬 기준은")
        @Nested
        class findOrder {

            BookmarkFindRequest request = BookmarkFindRequestFactory.onlyCount(hopesBookmarks.size());

            @DisplayName("생성시간 기준 내림차순으로 한다")
            @Test
            void orderByCreatedDateDesc() {
                List<BookmarkResponse> foundBookmarks = bookmarkService.find(request, hope.getId()).getBookmarks();
                List<Long> orderedBookmarkIds = extractIdsOrderByCreatedTimeDesc(hopesBookmarks);

                assertThat(foundBookmarks).extracting("id").containsExactlyElementsOf(orderedBookmarkIds);
            }
        }

        @DisplayName("요청 파라미터가 전부 없다면")
        @Nested
        class emptyParams {

            BookmarkFindRequest request = BookmarkFindRequestFactory.emptyQueryParams();

            @DisplayName("최근에 등록한 북마크부터 20개를 조회한다")
            @Test
            void twentyBookmarksFromLatest() {
                List<BookmarkResponse> foundBookmarks = bookmarkService.find(request, hope.getId()).getBookmarks();
                List<Long> expectedIds = extractIdsOrderByCreatedTimeDescDefaultLimit(hopesBookmarks);

                assertThat(foundBookmarks).extracting("id").isEqualTo(expectedIds);
            }
        }

        @DisplayName("요청 파라미터에 개수만 있다면")
        @Nested
        class onlyCountInParams {

            int count = 3;
            BookmarkFindRequest request = BookmarkFindRequestFactory.onlyCount(count);

            @DisplayName("해당 개수만큼 조회한다")
            @Test
            void limitCount() {
                List<BookmarkResponse> foundBookmarks = bookmarkService.find(request, hope.getId()).getBookmarks();

                assertThat(foundBookmarks).hasSize(count);
            }
        }

        @DisplayName("요청 파라미터에 북마크 id만 있다면")
        @Nested
        class onlyBookmarkIdInParams {

            Bookmark targetBookmark = hopesBookmarks.get(hopesBookmarks.size() - 1);
            BookmarkFindRequest request = BookmarkFindRequestFactory.onlyBookmarkId(targetBookmark.getId());

            @DisplayName("해당 북마크보다 과거에 추가된 북마크를 20개 조회한다")
            @Test
            void findCreatedBefore() {
                List<BookmarkResponse> foundBookmarks = bookmarkService.find(request, hope.getId()).getBookmarks();

                assertAll(
                        () -> assertThat(foundBookmarks).isNotEmpty(),
                        () -> assertThat(foundBookmarks).allMatch(bookmark -> bookmark.getId() < targetBookmark.getId())
                );
            }
        }

        @DisplayName("요청 파라미터에 북마크 id와 count가 모두 있다면")
        @Nested
        class bookmarkIdAndCountInParams {

            Bookmark targetBookmark = hopesBookmarks.get(hopesBookmarks.size() - 1);
            int count = 5;
            BookmarkFindRequest request = BookmarkFindRequestFactory.bookmarkIdAndCount(targetBookmark.getId(), count);

            @DisplayName("해당 북마크보다 과거에 추가된 북마크를 count개 조회한다")
            @Test
            void findCountBookmarksCreatedBeforeTarget() {
                List<BookmarkResponse> foundBookmarks = bookmarkService.find(request, hope.getId()).getBookmarks();

                assertAll(
                        () -> assertThat(foundBookmarks).hasSize(count),
                        () -> assertThat(foundBookmarks).allMatch(bookmark -> bookmark.getId() < targetBookmark.getId())
                );
            }
        }

        private Member saveMember(final MemberFixture memberFixture, final Workspace workspace) {
            Member member = memberFixture.create(workspace);
            member.firstLogin("xoxp-" + member.getSlackId());
            return members.save(member);
        }

        private List<Message> createAndSaveMessages(final Channel channel, final Member member) {
            List<Message> messagesInChannel = Arrays.stream(MessageFixtures.values())
                    .map(messageFixture -> messageFixture.create(channel, member))
                    .collect(Collectors.toList());

            for (Message fixture : messagesInChannel) {
                messages.save(fixture);
            }

            return messagesInChannel;
        }

        private List<Bookmark> saveBookmarksAtDifferentTimes(final Member member, final List<Message> messages) {
            List<Bookmark> savedBookmarks = new ArrayList<>();
            int totalSize = messages.size();

            for (int i = 0; i < totalSize; i++) {
                given(dateTimeProvider.getNow()).willReturn(
                        Optional.of(LocalDateTime.of(2022, 9, 1, 0, 0, 0).plusHours(i)));

                Bookmark bookmark = bookmarks.save(new Bookmark(member, messages.get(totalSize - i - 1)));
                savedBookmarks.add(bookmark);
            }

            return savedBookmarks;
        }

        private List<Long> extractIds(final List<Bookmark> bookmarks) {
            return bookmarks.stream()
                    .map(Bookmark::getId)
                    .collect(Collectors.toList());
        }

        private List<Long> extractIdsOrderByCreatedTimeDesc(final List<Bookmark> bookmarks) {
            return bookmarks.stream()
                    .sorted(Comparator.comparing(Bookmark::getCreatedDate).reversed())
                    .map(Bookmark::getId)
                    .collect(Collectors.toList());
        }

        private List<Long> extractIdsOrderByCreatedTimeDescDefaultLimit(final List<Bookmark> bookmarks) {
            return bookmarks.stream()
                    .sorted(Comparator.comparing(Bookmark::getCreatedDate).reversed())
                    .limit(20)
                    .map(Bookmark::getId)
                    .collect(Collectors.toList());
        }
    }
}
