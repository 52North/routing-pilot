package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.GeometryCollection;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class GeometryCollectionWriteConverter extends AbstractGeometryWriteConverter<GeometryCollection> {
    public static final GeometryCollectionWriteConverter INSTANCE = new GeometryCollectionWriteConverter();

    public GeometryCollectionWriteConverter() {
        super(GeometryCollection.class);
    }

}
