package com.pickpick.utils;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);;

    public static <T> T convert(final String json, final Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // TODO
        }
    }
}
