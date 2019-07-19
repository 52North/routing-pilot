package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.MultiLineString;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class MultiLineStringWriteConverter extends AbstractGeometryWriteConverter<MultiLineString> {
    public static final MultiLineStringWriteConverter INSTANCE = new MultiLineStringWriteConverter();

    public MultiLineStringWriteConverter() {
        super(MultiLineString.class);
    }
}
