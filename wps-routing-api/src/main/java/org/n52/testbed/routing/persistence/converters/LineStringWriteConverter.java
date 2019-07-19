package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.LineString;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class LineStringWriteConverter extends AbstractGeometryWriteConverter<LineString> {
    public static final LineStringWriteConverter INSTANCE = new LineStringWriteConverter();

    public LineStringWriteConverter() {
        super(LineString.class);
    }
}
