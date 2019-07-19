package org.n52.testbed.routing.persistence.converters;

import org.locationtech.jts.geom.GeometryCollection;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class GeometryCollectionReadConverter extends AbstractGeometryReadConverter<GeometryCollection> {
    public static final GeometryCollectionReadConverter INSTANCE = new GeometryCollectionReadConverter();

    public GeometryCollectionReadConverter() {
        super(GeometryCollection.class);
    }

}
