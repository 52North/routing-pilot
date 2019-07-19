package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.MultiPolygon;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class MultiPolygonWriteConverter extends AbstractGeometryWriteConverter<MultiPolygon> {
    public static final MultiPolygonWriteConverter INSTANCE = new MultiPolygonWriteConverter();

    public MultiPolygonWriteConverter() {
        super(MultiPolygon.class);
    }
}
