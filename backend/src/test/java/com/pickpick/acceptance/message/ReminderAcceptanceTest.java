package com.pickpick.acceptance.message;

import static com.pickpick.acceptance.RestHandler.상태코드_200_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_201_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_204_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_400_확인;
import static com.pickpick.acceptance.RestHandler.상태코드_404_확인;
import static com.pickpick.acceptance.auth.AuthRestHandler.워크스페이스_초기화_및_로그인;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_단건_조회;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_목록_조회;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_삭제;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_생성;
import static com.pickpick.acceptance.message.ReminderRestHandler.리마인더_수정;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_목록_생성;
import static com.pickpick.acceptance.slackevent.SlackEventRestHandler.메시지_전송;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pickpick.acceptance.AcceptanceTest;
import com.pickpick.acceptance.message.ReminderRestHandler.ReminderFindRequest;
import com.pickpick.fixture.MemberFixture;
import com.pickpick.message.ui.dto.ReminderResponse;
import com.pickpick.message.ui.dto.ReminderResponses;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("리마인더 인수 테스트")
@SuppressWarnings("NonAsciiCharacters")
public class ReminderAcceptanceTest extends AcceptanceTest {

    private static final String MEMBER_SLACK_ID = MemberFixture.findFirst().getSlackId();

    @BeforeEach
    void init() {
        워크스페이스_초기화_및_로그인(MEMBER_SLACK_ID);
    }

    @Test
    void 리마인더_생성_검증() {
        // given
        String token = jwtTokenProvider.createToken("1");
        메시지_전송(MEMBER_SLACK_ID);

        // when
        ExtractableResponse<Response> response = 리마인더_생성(token, 1, LocalDateTime.now());

        // then
        상태코드_201_확인(response);
    }

    @Test
    void 리마인더_단건_조회_정상_응답() {
        // given
        String token = jwtTokenProvider.createToken("1");
        메시지_전송(MEMBER_SLACK_ID);
        리마인더_생성(token, 1, LocalDateTime.now());

        // when
        ExtractableResponse<Response> response = 리마인더_단건_조회(token, 1L);

        // then
        상태코드_200_확인(response);
        ReminderResponse reminderResponse = response.jsonPath().getObject("", ReminderResponse.class);
        assertThat(reminderResponse.getId()).isEqualTo(1L);
    }

    @Test
    void 존재하지_않는_리마인더_조회시_404_응답() {
        // given
        String token = jwtTokenProvider.createToken("1");
        메시지_전송(MEMBER_SLACK_ID);

        // when
        ExtractableResponse<Response> response = 리마인더_단건_조회(token, 1L);

        // then
        상태코드_404_확인(response);
    }

    @Test
    void 특정_멤버가_리마인더_등록한_메시지_목록_조회() {
        // given
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 10;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 3L, 5L, 7L);
        리마인더_목록_생성(token, messageIdsForReminder);

        ReminderFindRequest request = new ReminderFindRequest();

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, request);

        // then
        상태코드_200_확인(response);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertAll(
                () -> assertThat(reminderResponses.hasFuture()).isFalse(),
                () -> assertThat(convertToMessageIds(reminderResponses)).containsExactlyElementsOf(
                        messageIdsForReminder)
        );
    }

    @Test
    void 특정_리마인더보다_미래시간에_등록된_리마인더_목록_조회() {
        // given
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 5;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 2L, 3L, 4L, 5L);
        리마인더_목록_생성(token, messageIdsForReminder);

        List<Long> expectedMessageIds = List.of(3L, 4L, 5L);

        ReminderFindRequest request = new ReminderFindRequest()
                .reminderId(2L);

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, request);

        // then
        상태코드_200_확인(response);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertAll(
                () -> assertThat(reminderResponses.hasFuture()).isFalse(),
                () -> assertThat(convertToMessageIds(reminderResponses)).containsExactlyElementsOf(expectedMessageIds)
        );
    }

    @Test
    void 리마인더_조회_시_가장_최신인_리마인더가_포함된다면_hasFuture가_False다() {
        // given
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 11;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L);
        리마인더_목록_생성(token, messageIdsForReminder);

        ReminderFindRequest request = new ReminderFindRequest()
                .reminderId(3L)
                .count(10);

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, request);

        // then
        상태코드_200_확인(response);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertThat(reminderResponses.hasFuture()).isFalse();
    }

    @Test
    void 리마인더_조회_시_가장_최신인_리마인더가_포함되지_않는다면_hasFuture가_True다() {
        // given
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 11;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L);
        리마인더_목록_생성(token, messageIdsForReminder);

        ReminderFindRequest request = new ReminderFindRequest()
                .count(10);

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, request);

        // then
        상태코드_200_확인(response);

        ReminderResponses reminderResponses = response.jsonPath().getObject("", ReminderResponses.class);
        assertThat(reminderResponses.hasFuture()).isTrue();
    }

    @Test
    void 리마인더_조회_시_count_값이_없으면_20개가_조회된다() {
        // given
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 20;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L,
                16L, 17L, 18L, 19L, 20L);
        리마인더_목록_생성(token, messageIdsForReminder);

        ReminderFindRequest request = new ReminderFindRequest();

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(리마인더_개수(response)).isEqualTo(20);
    }

    @Test
    void 리마인더_조회_시_count_값이_있다면_count_개수_만큼_조회된다() {
        // given
        String token = jwtTokenProvider.createToken("1");

        int messageCount = 20;
        메시지_목록_생성(MEMBER_SLACK_ID, messageCount);

        List<Long> messageIdsForReminder = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L,
                16L, 17L, 18L, 19L, 20L);
        리마인더_목록_생성(token, messageIdsForReminder);

        int reminderCount = 10;

        ReminderFindRequest request = new ReminderFindRequest()
                .count(reminderCount);

        // when
        ExtractableResponse<Response> response = 리마인더_목록_조회(token, request);

        // then
        상태코드_200_확인(response);
        assertThat(리마인더_개수(response)).isEqualTo(reminderCount);
    }

    @Test
    void 리마인더_정상_수정() {
        // given
        String token = jwtTokenProvider.createToken("1");
        메시지_전송(MEMBER_SLACK_ID);

        long messageId = 1L;
        리마인더_생성(token, messageId, LocalDateTime.now().plusDays(1));

        // when
        ExtractableResponse<Response> response = 리마인더_수정(token, messageId, LocalDateTime.now());

        // then
        상태코드_200_확인(response);
    }

    @Test
    void 사용자에게_존재하지_않는_리마인더_수정() {
        // given
        String token1 = jwtTokenProvider.createToken("1");
        String token2 = jwtTokenProvider.createToken("2");

        메시지_전송(MEMBER_SLACK_ID);
        리마인더_생성(token1, 1, LocalDateTime.now().plusDays(1));

        // when
        ExtractableResponse<Response> response = 리마인더_수정(token2, 1L, LocalDateTime.now());

        // then
        상태코드_400_확인(response);
    }

    @Test
    void 리마인더_정상_삭제() {
        // given
        String token = jwtTokenProvider.createToken("1");
        메시지_전송(MEMBER_SLACK_ID);

        long messageId = 1L;
        리마인더_생성(token, messageId, LocalDateTime.now());

        // when
        ExtractableResponse<Response> response = 리마인더_삭제(token, messageId);

        // then
        상태코드_204_확인(response);
    }

    @Test
    void 사용자에게_존재하지_않는_리마인더_삭제() {
        // given
        long messageId = 1L;
        String token = jwtTokenProvider.createToken("1");

        // when
        ExtractableResponse<Response> response = 리마인더_삭제(token, messageId);

        // then
        상태코드_400_확인(response);
    }

    private void 리마인더_목록_생성(final String token, final List<Long> messageIds) {
        int size = messageIds.size();
        for (int i = 0; i < size; i++) {
            리마인더_생성(token, messageIds.get(i), LocalDateTime.now().plusDays(i + 1));
        }
    }

    private List<Long> convertToMessageIds(final ReminderResponses response) {
        return response.getReminders()
                .stream()
                .map(ReminderResponse::getMessageId)
                .collect(Collectors.toList());
    }

    private int 리마인더_개수(final ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject("", ReminderResponses.class)
                .getReminders()
                .size();
    }
}
