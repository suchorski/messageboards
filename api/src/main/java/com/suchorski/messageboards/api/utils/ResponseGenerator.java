package com.suchorski.messageboards.api.utils;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ResponseGenerator {

    public Map<String, Object> content(Object content) {
        return Map.of("content", content);
    }

}
