package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.MultiPoint;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class MultiPointReadConverter extends AbstractGeometryReadConverter<MultiPoint> {
    public static final MultiPointReadConverter INSTANCE = new MultiPointReadConverter();

    public MultiPointReadConverter() {
        super(MultiPoint.class);
    }
}
