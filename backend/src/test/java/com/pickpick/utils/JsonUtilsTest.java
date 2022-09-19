package com.pickpick.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pickpick.exception.utils.InvalidJsonRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonUtilsTest {

    @DisplayName(value = "유효한 JSON 형식")
    @Test
    void convert() {
        // given
        String json = "{\"a\":\"aaa\",\"b\":\"bbb\"}";

        // when
        TestClass actual = JsonUtils.convert(json, TestClass.class);

        // then
        assertThat(actual.getA()).isEqualTo("aaa");
        assertThat(actual.getB()).isEqualTo("bbb");
    }

    @DisplayName(value = "유효하지 않은 JSON 형식")
    @Test
    void cannotConvert() {
        // given
        String json = "aaa";

        // when & then
        assertThatThrownBy(() -> JsonUtils.convert(json, TestClass.class))
                .isInstanceOf(InvalidJsonRequestException.class);
    }

    private static class TestClass {

        private String a;
        private String b;

        private TestClass() {
        }

        public TestClass(final String a, final String b) {
            this.a = a;
            this.b = b;
        }

        public String getA() {
            return a;
        }

        public String getB() {
            return b;
        }
    }
}
