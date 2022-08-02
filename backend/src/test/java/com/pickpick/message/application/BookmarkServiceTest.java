package com.pickpick.message.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.ui.dto.BookmarkRequest;
import com.pickpick.message.ui.dto.BookmarkResponse;
import com.pickpick.message.ui.dto.BookmarkResponses;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/truncate.sql", "/bookmark.sql"})
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

    @DisplayName("북마크를 생성한다")
    @Test
    void save() {
        // given
        Member member = new Member("U1234", "사용자", "user.png");
        members.save(member);
        Channel channel = new Channel("C1234", "기본채널");
        channels.save(channel);
        Message message = new Message("M1234", "메시지", member, channel, LocalDateTime.now(), LocalDateTime.now());
        messages.save(message);

        BookmarkRequest bookmarkRequest = new BookmarkRequest(message.getId());
        int beforeSize = findBookmarksSize(member);

        // when
        bookmarkService.save(member.getId(), bookmarkRequest);

        // then
        int afterSize = findBookmarksSize(member);
        assertThat(beforeSize + 1).isEqualTo(afterSize);
    }

    private int findBookmarksSize(final Member member) {
        return bookmarkService.find(null, member.getId()).getBookmarks().size();
    }

    @DisplayName("북마크 조회")
    @ParameterizedTest(name = "{0}")
    @MethodSource("parameterProvider")
    void findBookmarks(final String subscription, final Long bookmarkId, final Long memberId,
                       final List<Long> expectedIds, final boolean expectedIsLast) {
        // given & when
        BookmarkResponses response = bookmarkService.find(bookmarkId, memberId);

        // then
        List<Long> ids = convertToIds(response);
        assertAll(
                () -> assertThat(ids).containsExactlyElementsOf(expectedIds),
                () -> assertThat(response.isLast()).isEqualTo(expectedIsLast)
        );
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(
                Arguments.arguments("멤버 ID 2번으로 북마크를 조회한다", null, 2L, List.of(1L), true),
                Arguments.arguments("멤버 ID가 1번이고 북마크 id 23번일 때 북마크 목록을 조회한다", 23L, 1L,
                        List.of(22L, 21L, 20L, 19L, 18L, 17L, 16L, 15L, 14L, 13L, 12L, 11L, 10L, 9L, 8L, 7L, 6L, 5L, 4L,
                                3L), false),
                Arguments.arguments("북마크 조회 시 가장 오래된 북마크가 포함된다면 isLast가 true이다", null, 2L, List.of(1L), true)
        );
    }

    private List<Long> convertToIds(final BookmarkResponses response) {
        return response.getBookmarks().stream().map(BookmarkResponse::getId).collect(Collectors.toList());
    }
}
