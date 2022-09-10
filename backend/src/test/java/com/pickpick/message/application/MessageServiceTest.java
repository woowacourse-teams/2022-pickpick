package com.pickpick.message.application;

import static com.pickpick.fixture.ChannelFactory.공지사항;
import static com.pickpick.fixture.ChannelFactory.잡담;
import static com.pickpick.fixture.MemberFactory.써머;
import static com.pickpick.fixture.MessageRequestFactory.검색대상_여러채널과_키워드로_요청;
import static com.pickpick.fixture.MessageRequestFactory.여러채널에서_기준_메시지의_과거_내림차순_요청;
import static com.pickpick.fixture.MessageRequestFactory.여러채널에서_기준_메시지의_미래_오름차순_요청;
import static com.pickpick.fixture.MessageRequestFactory.여러채널의_최신부터_내림차순_요청;
import static com.pickpick.fixture.MessageRequestFactory.쿼리_파라미터가_없는_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.pickpick.config.DatabaseCleaner;
import com.pickpick.channel.domain.Channel;
import com.pickpick.channel.domain.ChannelRepository;
import com.pickpick.channel.domain.ChannelSubscription;
import com.pickpick.channel.domain.ChannelSubscriptionRepository;
import com.pickpick.fixture.MessageFixtures;
import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import com.pickpick.message.domain.Message;
import com.pickpick.message.domain.MessageRepository;
import com.pickpick.message.ui.dto.MessageRequest;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.StringUtils;

@Sql({"/message.sql"})
@SpringBootTest
class MessageServiceTest {

    private static final long MEMBER_ID = 1L;
    public static final int MESSAGE_COUNT = 2;
    public static final int TARGET_INDEX = 2;
    private static final String MESSAGE_KEYWORD = "줍줍";

    @Autowired
    private MemberRepository members;

    @Autowired
    private ChannelRepository channels;

    @Autowired
    private ChannelSubscriptionRepository subscriptions;

    @Autowired
    private MessageRepository messages;

    @Autowired
    private MessageService messageService;
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @SpyBean
    private Clock clock;

    @DisplayName("쿼리 파라미터가 모두 없다면, 회원의 첫번째 순서 구독 채널의 최신 메시지를 내림차순 조회한다")
    @Test
    void findMessagesEmptyParameters() {
        // given
        Member 써머 = members.save(써머());
        Channel 공지사항 = channels.save(공지사항());
        Channel 잡담 = channels.save(잡담());
        List<Message> 잡담_메시지 = messages.saveAll(모든_메시지_생성(잡담, 써머));

        subscriptions.save(new ChannelSubscription(잡담, 써머, 1));
        subscriptions.save(new ChannelSubscription(공지사항, 써머, 2));

        MessageRequest request = 쿼리_파라미터가_없는_요청(MESSAGE_COUNT);

        // when
        MessageResponses response = messageService.find(써머.getId(), request);

        // then
        List<MessageResponse> 조회된_메시지_목록 = response.getMessages();
        assertAll(
                () -> assertThat(조회된_메시지_목록).extracting("id").isSubsetOf(id_시간_내림차순_추출(잡담_메시지)),
                () -> assertThat(조회된_메시지_목록).hasSize(MESSAGE_COUNT)
        );
    }

    @DisplayName("더 이상 조회할 메시지가 없다면 isLast로 true를 반환한다")
    @Test
    void findMessagesIsLast() {
        // given
        Member 써머 = members.save(써머());
        Channel 공지사항 = channels.save(공지사항());
        List<Message> 공지사항_메시지 = messages.saveAll(모든_메시지_생성(공지사항, 써머));
        subscriptions.save(new ChannelSubscription(공지사항, 써머, 1));

        MessageRequest request = 쿼리_파라미터가_없는_요청(공지사항_메시지.size());

        // when
        MessageResponses response = messageService.find(써머.getId(), request);

        // then
        assertThat(response.isLast()).isEqualTo(true);
    }

    private static Stream<Arguments> messageRequestWithReminder() {
        return Stream.of(
                Arguments.of("현재 시간보다 오래된 리마인더가 존재하면 isSetReminded가 false이다",
                        "2022-08-13T00:00:00Z",
                        new MessageRequest("", "", List.of(5L), true, null, 1),
                        false),
                Arguments.of("현재 시간보다 최신인 리마인더가 존재하면 isSetReminded가 true이다",
                        "2022-08-10T00:00:00Z",
                        new MessageRequest("", "", List.of(5L), true, null, 1),
                        true),
                Arguments.of("현재 시간과 동일한 리마인더가 존재하면 isSetReminded가 false이다",
                        "2022-08-12T14:20:00Z",
                        new MessageRequest("", "", List.of(5L), true, null, 1),
                        false)
        );
    }

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @DisplayName("메시지 조회 시, 텍스트가 비어있는 메시지는 필터링된다")
    @Test
    void emptyMessagesShouldBeFiltered() {
        // given
        Member 써머 = members.save(써머());
        Channel 공지사항 = channels.save(공지사항());
        List<Message> 공지사항_메시지 = messages.saveAll(모든_메시지_생성(공지사항, 써머));

        MessageRequest request = 여러채널의_최신부터_내림차순_요청(List.of(공지사항), 공지사항_메시지.size());

        // when
        MessageResponses messageResponses = messageService.find(써머.getId(), request);

        // then
        List<MessageResponse> 조회된_메시지 = messageResponses.getMessages();

        assertThat(조회된_메시지).extracting("text").allMatch(text -> StringUtils.hasText((String) text));
    }

    @DisplayName("여러 채널의 특정 키워드로 조회 시, 해당 키워드가 존재하는 메시지만 조회된다")
    @Test
    void findMessagesInChannelsWithKeyword() {
        // given
        Member 써머 = members.save(써머());
        Channel 공지사항 = channels.save(공지사항());
        Channel 잡담 = channels.save(잡담());

        messages.save(MessageFixtures.PLAIN_20220712_14_00_00.생성(공지사항, 써머));
        messages.save(MessageFixtures.PLAIN_20220712_15_00_00.생성(잡담, 써머));
        messages.save(MessageFixtures.KEYWORD_20220714_14_00_00.생성(잡담, 써머));

        MessageRequest request = 검색대상_여러채널과_키워드로_요청(List.of(잡담), MESSAGE_KEYWORD, MESSAGE_COUNT);

        // when
        MessageResponses response = messageService.find(써머.getId(), request);

        // then
        List<MessageResponse> 조회된_메시지 = response.getMessages();
        assertAll(
                () -> assertThat(조회된_메시지).hasSize(1),
                () -> assertThat(조회된_메시지).extracting("text").allMatch(text -> ((String) text).contains(MESSAGE_KEYWORD))
        );
    }

    @DisplayName("채널과 메시지ID로 앞선 메시지 조회 시, 메시지 기준 해당 채널의 미래 메시지를 시간 내림차순 조회한다")
    @Test
    void findMessagesWithChannelAndMessageNotNeedPast() {
        // given
        Member 써머 = members.save(써머());
        Channel 공지사항 = channels.save(공지사항());
        List<Message> 공지사항_메시지 = messages.saveAll(모든_메시지_생성(공지사항, 써머));
        Message 기준_메시지 = 공지사항_메시지.get(TARGET_INDEX);

        MessageRequest request = 여러채널에서_기준_메시지의_과거_내림차순_요청(List.of(공지사항), 기준_메시지, MESSAGE_COUNT);

        // when
        MessageResponses response = messageService.find(써머.getId(), request);

        // then
        List<MessageResponse> 조회된_메시지 = response.getMessages();
        assertAll(
                () -> assertThat(조회된_메시지).extracting("id").isSubsetOf(id_시간_내림차순_추출(공지사항_메시지)),
                () -> assertThat(조회된_메시지).allMatch(message -> message.getPostedDate().isBefore(기준_메시지.getPostedDate()))
        );
    }

    @DisplayName("채널과 메시지ID로 지난 메시지 조회 시, 메시지 기준 해당 채널의 과거 메시지를 시간 오름차순 조회한다")
    @Test
    void findMessagesWithChannelAndMessageNeedPast() {
        // given
        Member 써머 = members.save(써머());
        Channel 공지사항 = channels.save(공지사항());
        List<Message> 공지사항_메시지 = messages.saveAll(모든_메시지_생성(공지사항, 써머));
        Message 기준_메시지 = 공지사항_메시지.get(TARGET_INDEX);

        MessageRequest request = 여러채널에서_기준_메시지의_미래_오름차순_요청(List.of(공지사항), 기준_메시지, MESSAGE_COUNT);

        // when
        MessageResponses response = messageService.find(써머.getId(), request);

        // then
        List<MessageResponse> 조회된_메시지 = response.getMessages();
        assertAll(
                () -> assertThat(조회된_메시지).extracting("id").isSubsetOf(id_시간_오름차순_추출(공지사항_메시지)),
                () -> assertThat(조회된_메시지).allMatch(message -> message.getPostedDate().isAfter(기준_메시지.getPostedDate()))
        );
    }

    private List<Long> id_시간_내림차순_추출(final List<Message> messages) {
        return messages.stream()
                .sorted(Comparator.comparing(Message::getPostedDate).reversed())
                .map(Message::getId)
                .collect(Collectors.toList());
    }

    private List<Long> id_시간_오름차순_추출(final List<Message> messages) {
        return messages.stream()
                .sorted(Comparator.comparing(Message::getPostedDate))
                .map(Message::getId)
                .collect(Collectors.toList());
    }

    private List<Message> 모든_메시지_생성(final Channel channel, final Member member) {
        return Arrays.stream(MessageFixtures.values())
                .map(messageFixture -> messageFixture.생성(channel, member))
                .collect(Collectors.toList());
    }

    @DisplayName("메시지 조회 시 리마인더 여부 함께 조회된다")
    @MethodSource("messageRequestWithReminder")
    @ParameterizedTest(name = "{0}")
    void findSetRemindedMessage(final String description, final String nowDate, final MessageRequest messageRequest,
                                final boolean expected) {
        // given
        given(clock.instant())
                .willReturn(Instant.parse(nowDate));

        // when
        MessageResponse message = messageService.find(MEMBER_ID, messageRequest)
                .getMessages()
                .get(0);

        // then
        assertThat(message.isSetReminded()).isEqualTo(expected);
    }

    @DisplayName("메시지 조회 시, remindDate가 함께 전달된다")
    @Test
    void checkRemindDate2() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));

        MessageRequest request = new MessageRequest("", "", List.of(5L), true, null, 1);

        // when
        MessageResponse message = messageService.find(MEMBER_ID, request)
                .getMessages()
                .get(0);

        // then
        assertThat(message.getRemindDate()).isEqualTo(LocalDateTime.of(2022, 8, 12, 14, 20, 0));
    }

    @DisplayName("메시지 조회 시, remindDate 값이 없으면 빈 값으로 전달된다")
    @Test
    void checkRemindDate() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-12T14:20:00Z"));

        MessageRequest request = new MessageRequest("", "", List.of(5L), true, null, 1);

        // when
        MessageResponse message = messageService.find(MEMBER_ID, request)
                .getMessages()
                .get(0);

        // then
        assertThat(message.getRemindDate()).isNull();
    }
}
