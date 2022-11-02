package com.pickpick.support;

import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FunctionWrapper {

    public static <T, R> Function<T, R> apply(ThrowingFunction<T, R, Exception> function) {
        return i -> {
            try {
                return function.apply(i);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        };
    }

    private FunctionWrapper() {
    }
}
