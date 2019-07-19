package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class GeometryReadConverter extends AbstractGeometryReadConverter<Geometry> {
    public static final GeometryReadConverter INSTANCE = new GeometryReadConverter();

    public GeometryReadConverter() {
        super(Geometry.class);
    }
}
