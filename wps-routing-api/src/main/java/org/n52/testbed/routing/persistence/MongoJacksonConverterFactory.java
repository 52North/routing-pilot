package org.n52.testbed.routing.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MongoJacksonConverterFactory {
    private final ObjectMapper objectMapper;

    @Autowired
    public MongoJacksonConverterFactory(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    public <T> MongoJacksonConverter<T> forType(Class<T> type) {
        return new MongoJacksonConverter<>(type, objectMapper);
    }
}
