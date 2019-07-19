package org.n52.jackson.datatype.jts;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Type safe variant of a JsonDeserializer that checks the parsed value is of the right type.
 *
 * @param <T> The type to be accepted.
 */
public class TypeSafeJsonDeserializer<T> extends JsonDeserializer<T> {
    private final JsonDeserializer<? super T> delegate;
    private final JavaType type;

    /**
     * Creates a new {@link TypeSafeJsonDeserializer}.
     *
     * @param type     The type to be accepted.
     * @param delegate The {@link JsonDeserializer} to delegate to.
     */
    public TypeSafeJsonDeserializer(Class<? extends T> type, JsonDeserializer<? super T> delegate) {
        this(TypeFactory.defaultInstance().constructType(type), delegate);
    }

    /**
     * Creates a new {@link TypeSafeJsonDeserializer}.
     *
     * @param type     The type to be accepted.
     * @param delegate The {@link JsonDeserializer} to delegate to.
     */
    public TypeSafeJsonDeserializer(JavaType type, JsonDeserializer<? super T> delegate) {
        this.delegate = Objects.requireNonNull(delegate);
        this.type = Objects.requireNonNull(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(JsonParser p, DeserializationContext context) throws IOException {
        Object obj = delegate.deserialize(p, context);
        if (obj == null) {
            return null;
        } else if (type.isTypeOrSuperTypeOf(obj.getClass())) {
            return (T) obj;
        } else {
            throw JsonMappingException.from(context, String.format("Invalid type for %s: %s", type, obj.getClass()));
        }
    }
}
