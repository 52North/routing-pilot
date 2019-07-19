package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.Polygon;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class PolygonWriteConverter extends AbstractGeometryWriteConverter<Polygon> {
    public static final PolygonWriteConverter INSTANCE = new PolygonWriteConverter();

    public PolygonWriteConverter() {
        super(Polygon.class);
    }
}
