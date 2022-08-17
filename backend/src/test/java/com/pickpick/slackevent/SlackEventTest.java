package com.pickpick.slackevent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.exception.slackevent.SlackEventNotFoundException;
import com.pickpick.slackevent.application.SlackEvent;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SlackEventTest {

    private static Stream<Arguments> methodSource() {
        return Stream.of(
                Arguments.of(Map.of("event", Map.of("type", "message")), SlackEvent.MESSAGE_CREATED),
                Arguments.of(Map.of("event", Map.of("type", "message", "subtype", "message_changed")),
                        SlackEvent.MESSAGE_CHANGED),
                Arguments.of(Map.of("event", Map.of("type", "message", "subtype", "message_deleted")),
                        SlackEvent.MESSAGE_DELETED),
                Arguments.of(Map.of("event", Map.of("type", "message", "subtype", "thread_broadcast")),
                        SlackEvent.MESSAGE_THREAD_BROADCAST),
                Arguments.of(Map.of("event", Map.of("type", "message", "subtype", "file_share")),
                        SlackEvent.MESSAGE_FILE_SHARE),
                Arguments.of(Map.of("event", Map.of("type", "channel_rename")), SlackEvent.CHANNEL_RENAME),
                Arguments.of(Map.of("event", Map.of("type", "channel_deleted")), SlackEvent.CHANNEL_DELETED),
                Arguments.of(Map.of("event", Map.of("type", "user_profile_changed")), SlackEvent.MEMBER_CHANGED),
                Arguments.of(Map.of("event", Map.of("type", "team_join")), SlackEvent.MEMBER_JOIN)
        );
    }

    @DisplayName("type과 subtype에 따라 SlackEvent 탐색")
    @ParameterizedTest
    @MethodSource("methodSource")
    void findSlackEventByTypeAndSubtype(final Map<String, Object> request, final SlackEvent expected) {
        // given & when
        SlackEvent actual = SlackEvent.of(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("존재하지 않는 SlackEvent type일 경우 예외 발생")
    @Test
    void notExistedSlackEvent() {
        // given
        Map<String, Object> request = Map.of("event", Map.of("존재하지 않는 type", "존재하지 않는 subtype"));

        // when & then
        assertThatThrownBy(() -> SlackEvent.of(request))
                .isInstanceOf(SlackEventNotFoundException.class);
    }
}
