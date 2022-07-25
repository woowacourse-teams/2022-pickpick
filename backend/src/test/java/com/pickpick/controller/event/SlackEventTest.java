package com.pickpick.controller.event;

import com.pickpick.exception.SlackEventNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SlackEventTest {

    @DisplayName("type과 subtype에 따라 SlackEvent 탐색")
    @ParameterizedTest
    @MethodSource("methodSource")
    void findSlackEventByTypeAndSubtype(Map<String, Object> request, SlackEvent expected) {
        // given & when
        SlackEvent actual = SlackEvent.of(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> methodSource() {
        return Stream.of(
                Arguments.of(Map.of("event", Map.of("type", "message")), SlackEvent.MESSAGE_CREATED),
                Arguments.of(Map.of("event", Map.of("type", "message", "subtype", "message_changed")),
                        SlackEvent.MESSAGE_CHANGED),
                Arguments.of(Map.of("event", Map.of("type", "message", "subtype", "message_deleted")),
                        SlackEvent.MESSAGE_DELETED)
        );
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
