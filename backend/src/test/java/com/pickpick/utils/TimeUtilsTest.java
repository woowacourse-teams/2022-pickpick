package com.pickpick.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TimeUtilsTest {

    @Test
    void toLocalDateTime() {
        // given
        String eventTimeStamp = "1657087057.056339";
        LocalDateTime expected = LocalDateTime.of(2022, 7, 6, 14, 57, 37);

        // when
        LocalDateTime actual = TimeUtils.toLocalDateTime(eventTimeStamp);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}