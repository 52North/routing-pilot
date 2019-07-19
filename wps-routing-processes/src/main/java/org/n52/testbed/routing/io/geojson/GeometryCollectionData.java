package org.n52.testbed.routing.io.geojson;

import org.locationtech.jts.geom.GeometryCollection;

public class GeometryCollectionData implements GeoJsonData<GeometryCollection> {
    private final GeometryCollection payload;

    public GeometryCollectionData(GeometryCollection payload) {
        this.payload = payload;
    }

    @Override
    public GeometryCollection getPayload() {
        return this.payload;
    }

    @Override
    public Class<GeometryCollection> getSupportedClass() {
        return GeometryCollection.class;
    }
}
