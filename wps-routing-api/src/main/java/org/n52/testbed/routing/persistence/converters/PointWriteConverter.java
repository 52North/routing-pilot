package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.Point;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class PointWriteConverter extends AbstractGeometryWriteConverter<Point> {
    public static final PointWriteConverter INSTANCE = new PointWriteConverter();

    public PointWriteConverter() {
        super(Point.class);
    }
}
