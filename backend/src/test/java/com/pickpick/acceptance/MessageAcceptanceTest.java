package com.pickpick.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.controller.dto.SlackMessageResponses;
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
@DisplayName("메시지 기능")
@SuppressWarnings("NonAsciiCharacters")
class MessageAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/messages";

    @MethodSource("methodSource")
    @ParameterizedTest(name = "{0}")
    void 메시지_조회_API(String description, Map<String, Object> request, boolean expectedIsLast,
                    List<Long> expectedMessageIds) {
        // when
        ExtractableResponse<Response> response = get(API_URL, request);

        // then
        SlackMessageResponses slackMessageResponses = response.as(SlackMessageResponses.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(slackMessageResponses.isLast()).isEqualTo(expectedIsLast),
                () -> assertThat(slackMessageResponses.getMessages())
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
                        createExpectedMessageIds(38L, 19L))
        );
    }

    private static List<Long> createExpectedMessageIds(Long endInclusive, Long startInclusive) {
        return LongStream.rangeClosed(startInclusive, endInclusive)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
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
}
