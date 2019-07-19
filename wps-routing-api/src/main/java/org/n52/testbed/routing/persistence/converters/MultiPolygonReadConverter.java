package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.MultiPolygon;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class MultiPolygonReadConverter extends AbstractGeometryReadConverter<MultiPolygon> {
    public static final MultiPolygonReadConverter INSTANCE = new MultiPolygonReadConverter();

    public MultiPolygonReadConverter() {
        super(MultiPolygon.class);
    }
}
