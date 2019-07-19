package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.MultiLineString;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class MultiLineStringReadConverter extends AbstractGeometryReadConverter<MultiLineString> {
    public static final MultiLineStringReadConverter INSTANCE = new MultiLineStringReadConverter();

    public MultiLineStringReadConverter() {
        super(MultiLineString.class);
    }
}
