package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.Point;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class PointReadConverter extends AbstractGeometryReadConverter<Point> {
    public static final PointReadConverter INSTANCE = new PointReadConverter();

    public PointReadConverter() {
        super(Point.class);
    }
}
