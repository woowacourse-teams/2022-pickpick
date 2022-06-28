package com.pickpick.controller.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
                Arguments.of(Map.of("event", Map.of("type", "message")), SlackEvent.MESSAGE_CREATED)
        );
    }
}
