package org.n52.testbed.routing.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MongoJacksonConverter<T> implements GenericConverter {
    private final Class<T> objectType;
    private final ObjectMapper objectMapper;

    public MongoJacksonConverter(Class<T> geometryType, ObjectMapper objectMapper) {
        this.objectType = Objects.requireNonNull(geometryType);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return new HashSet<>(Arrays.asList(new ConvertiblePair(objectType, Document.class),
                                           new ConvertiblePair(Document.class, objectType)));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        } else if (sourceType.getObjectType().equals(Document.class) &&
                   targetType.getObjectType().equals(this.objectType)) {
            return this.objectMapper.convertValue(source, this.objectType);
        } else if (sourceType.getObjectType().equals(this.objectType) &&
                   targetType.getObjectType().equals(Document.class)) {
            return this.objectMapper.convertValue(source, Map.class);
        } else {
            throw new IllegalArgumentException(String.format("unsupported types: %s -> %s", sourceType, targetType));
        }
    }
}