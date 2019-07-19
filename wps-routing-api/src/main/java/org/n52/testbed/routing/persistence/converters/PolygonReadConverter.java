package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.Polygon;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class PolygonReadConverter extends AbstractGeometryReadConverter<Polygon> {
    public static final PolygonReadConverter INSTANCE = new PolygonReadConverter();

    public PolygonReadConverter() {
        super(Polygon.class);
    }
}
