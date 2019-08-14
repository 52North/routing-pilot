package org.n52.testbed.routing.persistence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.Decimal128;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BsonModule extends SimpleModule {
    public BsonModule() {
        addSerializer(Decimal128.class, new Decimal128Serializer());
    }

    private static final class Decimal128Serializer extends JsonSerializer<Decimal128> {
        @Override
        public void serialize(Decimal128 value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value.isNaN()) {
                gen.writeNumber(Double.NaN);
            } else if (value.isInfinite()) {
                if (value.isNegative()) {
                    gen.writeNumber(Double.NEGATIVE_INFINITY);
                } else {
                    gen.writeNumber(Double.POSITIVE_INFINITY);
                }
            } else {
                gen.writeNumber(value.bigDecimalValue());
            }
        }
    }
}
