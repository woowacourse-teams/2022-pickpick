package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.message.ui.dto.MessageResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/truncate.sql", "/message.sql"})
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("메시지 기능")
@SuppressWarnings("NonAsciiCharacters")
class MessageAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/messages";
    private static final long MEMBER_ID = 1L;

    @MethodSource("methodSource")
    @ParameterizedTest(name = "{0}")
    void 메시지_조회_API(final String description, final Map<String, Object> request, final boolean expectedIsLast,
                    final List<Long> expectedMessageIds) {
        // when
        ExtractableResponse<Response> response = getWithAuthAndParams(API_URL, MEMBER_ID, request);

        // then
        MessageResponses messageResponses = response.as(MessageResponses.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(messageResponses.isLast()).isEqualTo(expectedIsLast),
                () -> assertThat(messageResponses.getMessages())
                        .extracting("id")
                        .isEqualTo(expectedMessageIds)
        );
    }

    private static Stream<Arguments> methodSource() {
        return Stream.of(
                Arguments.of(
                        "channelIds가 5이면, 5번 채널의 가장 최근 메시지 20개가 응답되어야 한다.",
                        createQueryParams("", "", "5", "", "", ""),
                        false,
                        createExpectedMessageIds(38L, 19L)),
                Arguments.of(
                        "channelIds가 5이고, needPastMessage가 true이고 date가 존재할 경우, 5번 채널의 해당 날짜 보다 과거 데이터 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("", "2022-07-13T19:21:55", "5", "true", "", ""),
                        true,
                        createExpectedMessageIds(6L, 1L)),
                Arguments.of(
                        "channelIds가 5이고, needPastMessage가 true이고 messageId가 존재할 경우, 5번 채널의 해당 메시지 보다 과거 데이터 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("", "", "5", "true", "6", ""),
                        true,
                        createExpectedMessageIds(5L, 1L)),
                Arguments.of(
                        "channelIds가 5이고, needPastMessage가 false이고 messageId가 존재할 경우, 5번 채널의 해당 메시지 보다 미래 데이터 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("", "", "5", "false", "6", ""),
                        false,
                        createExpectedMessageIds(26L, 7L)),
                Arguments.of(
                        "channelIds가 5이고, keyword가 '줍'일 경우, 5번 채널의 메시지 중 '줍'이 포함된 메시지 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("줍", "", "5", "", "", ""),
                        true,
                        createExpectedMessageIds(28L, 23L)),
                Arguments.of(
                        "channelIds가 5이고, keyword가 '호'이고, needPastMessage가 true이고 messageId가 존재할 경우, 5번 채널의 '호'가 포함된 메시지 중, 전달된 메시지 ID의 메시지보다 더 과거 메시지 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("호", "", "5", "", "13", ""),
                        true,
                        createExpectedMessageIds(7L, 4L)),
                Arguments.of(
                        "channelIds가 5이고, keyword가 'jupjup'일 경우, 5번 채널의 메시지 중 'jupjup'이 포함된 메시지 20개를 시간 내림차순으로 응답해야 한다.",
                        createQueryParams("jupjup", "", "5", "", "", ""),
                        true,
                        createExpectedMessageIds(18L, 14L))
        );
    }

    private static Map<String, String> createQueryParams(
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

    private static List<Long> createExpectedMessageIds(final Long endInclusive, final Long startInclusive) {
        return LongStream.rangeClosed(startInclusive, endInclusive)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}
