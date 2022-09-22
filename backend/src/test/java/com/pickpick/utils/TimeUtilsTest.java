package com.pickpick.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TimeUtilsTest {

    @Test
    void toLocalDateTime() {
        // given
        String eventTimeStamp = "1663748574.680000";
        LocalDateTime expected = LocalDateTime.of(2022, 9, 21, 17, 22, 54, 680000000);

        // when
        LocalDateTime actual = TimeUtils.toLocalDateTime(eventTimeStamp);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
