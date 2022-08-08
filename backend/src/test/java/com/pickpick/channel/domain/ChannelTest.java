package com.pickpick.channel.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.exception.channel.ChannelInvalidNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ChannelTest {

    @DisplayName("채널 이름은 빈 문자열, 또는 null로 변경할 수 없다")
    @NullAndEmptySource
    @ParameterizedTest
    void changeName(final String invalidName) {
        // given
        Channel channel = new Channel("slackId", "채널 이름");

        // when & then
        assertThatThrownBy(() -> channel.changeName(invalidName))
                .isInstanceOf(ChannelInvalidNameException.class);
    }
}
