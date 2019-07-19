package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.MultiPoint;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class MultiPointWriteConverter extends AbstractGeometryWriteConverter<MultiPoint> {
    public static final MultiPointWriteConverter INSTANCE = new MultiPointWriteConverter();

    public MultiPointWriteConverter() {
        super(MultiPoint.class);
    }

}
