package com.pickpick.acceptance.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.message.ui.dto.MessageResponse;
import com.pickpick.message.ui.dto.MessageResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.Clock;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/message.sql"})

@DisplayName("메시지 기능")
@SuppressWarnings("NonAsciiCharacters")
class MessageAcceptanceTest extends AcceptanceTest {

    private static final String MESSAGE_API_URL = "/api/messages";
    private static final long MEMBER_ID = 1L;

    @SpyBean
    private Clock clock;

    private static Stream<Arguments> methodSource() {
        return Stream.of(
                Arguments.of(
                        "channelIds가 5이면, 5번 채널의 가장 최근 메시지 20개가 응답되어야 한다.",
                        createQueryParams("", "", "5", "", "", ""),
                        false,
                        createExpectedMessageIds(38L, 19L),
                        true),
                Arguments.of(
                        "channelIds가 5이고, needPastMessage가 true이고 date가 존재할 경우, 5번 채널의 해당 날짜 보다 과거 데이터 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("", "2022-07-13T19:21:55", "5", "true", "", ""),
                        true,
                        createExpectedMessageIds(6L, 1L),
                        true),
                Arguments.of(
                        "channelIds가 5이고, needPastMessage가 true이고 messageId가 존재할 경우, 5번 채널의 해당 메시지 보다 과거 데이터 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("", "", "5", "true", "6", ""),
                        true,
                        createExpectedMessageIds(5L, 1L),
                        true),
                Arguments.of(
                        "channelIds가 5이고, needPastMessage가 false이고 messageId가 존재할 경우, 5번 채널의 해당 메시지 보다 미래 데이터 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("", "", "5", "false", "6", ""),
                        false,
                        createExpectedMessageIds(26L, 7L),
                        false),
                Arguments.of(
                        "channelIds가 5이고, keyword가 '줍'일 경우, 5번 채널의 메시지 중 '줍'이 포함된 메시지 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("줍", "", "5", "", "", ""),
                        true,
                        createExpectedMessageIds(28L, 23L),
                        true),
                Arguments.of(
                        "channelIds가 5이고, keyword가 '호'이고, needPastMessage가 true이고 messageId가 존재할 경우, 5번 채널의 '호'가 포함된 메시지 중, 전달된 메시지 ID의 메시지보다 더 과거 메시지 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("호", "", "5", "", "13", ""),
                        true,
                        createExpectedMessageIds(7L, 4L),
                        true),
                Arguments.of(
                        "channelIds가 5이고, keyword가 'jupjup'일 경우, 5번 채널의 메시지 중 'jupjup'이 포함된 메시지 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("jupjup", "", "5", "", "", ""),
                        true,
                        createExpectedMessageIds(18L, 14L),
                        true),
                Arguments.of(
                        "쿼리 파라미터가 전혀 전달되지 않았을 경우, 회원의 채널 정렬 상 첫번째 채널의 최신 20개 메시지를 작성시간 내림차순으로 응답해야 한다.",
                        createQueryParams("", "", "", "", "", ""),
                        false,
                        createExpectedMessageIds(38L, 19L),
                        true)
        );
    }

    private static Map<String, Object> createQueryParams(
            final String keyword, final String date, final String channelIds, final String needPastMessage,
            final String messageId, final String messageCount
    ) {
        return Map.of(
                "keyword", keyword,
                "date", date,
                "channelIds", channelIds,
                "needPastMessage", needPastMessage,
                "messageId", messageId,
                "messageCount", messageCount
        );
    }

    private static List<Long> createExpectedMessageIds(final Long startInclusive, final Long endInclusive) {
        return LongStream.rangeClosed(endInclusive, startInclusive)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    @MethodSource("methodSource")
    @ParameterizedTest(name = "{0}")
    void 메시지_조회_API(final String description, final Map<String, Object> request, final boolean expectedIsLast,
                    final List<Long> expectedMessageIds, final boolean expectedNeedPastMessage) {
        // given & when
        ExtractableResponse<Response> response = getWithCreateToken(MESSAGE_API_URL, MEMBER_ID, request);

        // then
        MessageResponses messageResponses = response.as(MessageResponses.class);

        assertAll(
                () -> 상태코드_200_확인(response),
                () -> assertThat(messageResponses.isLast()).isEqualTo(expectedIsLast),
                () -> assertThat(messageResponses.isNeedPastMessage()).isEqualTo(expectedNeedPastMessage),
                () -> assertThat(messageResponses.getMessages())
                        .extracting("id")
                        .isEqualTo(expectedMessageIds)
        );
    }

    @ValueSource(strings = {"", "true"})
    @ParameterizedTest
    void 메시지_조회_시_needPastMessage_true_응답_확인(final String needPastMessage) {
        // given
        Map<String, Object> request = createQueryParams("jupjup", "", "5", needPastMessage, "", "");

        // when
        ExtractableResponse<Response> response = getWithCreateToken(MESSAGE_API_URL, MEMBER_ID, request);
        MessageResponses messageResponses = response.as(MessageResponses.class);

        // then
        상태코드_200_확인(response);
        assertThat(messageResponses.isNeedPastMessage()).isTrue();
    }

    @Test
    void 메시지_조회_시_needPastMessage가_False일_경우_응답_확인() {
        // given
        Map<String, Object> request = createQueryParams("jupjup", "", "5", "false", "", "");

        // when
        ExtractableResponse<Response> response = getWithCreateToken(MESSAGE_API_URL, MEMBER_ID, request);
        MessageResponses messageResponses = response.as(MessageResponses.class);

        // then
        상태코드_200_확인(response);
        assertThat(messageResponses.isNeedPastMessage()).isFalse();
    }

    @Test
    void 이미_리마인드_완료된_메시지_조회_시_isSetReminded가_false이고_remindDate가_null() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-13T00:00:00Z"));
        Map<String, Object> request = createQueryParams("", "", "5", "true", "", "1");

        // when
        ExtractableResponse<Response> response = getWithCreateToken(MESSAGE_API_URL, MEMBER_ID, request);
        MessageResponse messageResponse = response.as(MessageResponses.class)
                .getMessages()
                .get(0);

        // then
        상태코드_200_확인(response);
        assertAll(
                () -> assertThat(messageResponse.isSetReminded()).isFalse(),
                () -> assertThat(messageResponse.getRemindDate()).isNull()
        );
    }

    @Test
    void 리마인드_해야하는_메시지_조회_시_isSetReminded가_true이고_remindDate에_값이_존재() {
        // given
        given(clock.instant())
                .willReturn(Instant.parse("2022-08-10T00:00:00Z"));
        Map<String, Object> request = createQueryParams("", "", "5", "true", "", "1");

        // when
        ExtractableResponse<Response> response = getWithCreateToken(MESSAGE_API_URL, MEMBER_ID, request);
        MessageResponse messageResponse = response.as(MessageResponses.class)
                .getMessages()
                .get(0);

        // then
        상태코드_200_확인(response);
        assertAll(
                () -> assertThat(messageResponse.isSetReminded()).isTrue(),
                () -> assertThat(messageResponse.getRemindDate()).isNotNull()
        );
    }
}
