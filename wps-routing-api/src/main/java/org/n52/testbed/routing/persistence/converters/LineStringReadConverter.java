package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.LineString;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class LineStringReadConverter extends AbstractGeometryReadConverter<LineString> {
    public static final LineStringReadConverter INSTANCE = new LineStringReadConverter();

    public LineStringReadConverter() {
        super(LineString.class);
    }

}
