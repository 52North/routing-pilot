package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class GeometryWriteConverter extends AbstractGeometryWriteConverter<Geometry> {
    public static final GeometryWriteConverter INSTANCE = new GeometryWriteConverter();

    public GeometryWriteConverter() {
        super(Geometry.class);
    }
}
