package com.cloudnativejava.dataservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    public static String writeValueAsString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T readValue(String jsonValue, Class<T> tClass) throws IOException {
        return objectMapper.readValue(jsonValue, tClass);
    }

    public static <T> T readValue(String jsonValue, TypeReference typeReference) throws IOException {
        return objectMapper.readValue(jsonValue, typeReference);
    }
}
