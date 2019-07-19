package org.n52.testbed.routing.io.geojson;

import org.locationtech.jts.geom.MultiPoint;

public class MultiPointData implements GeoJsonData<MultiPoint> {
    private final MultiPoint payload;

    public MultiPointData(MultiPoint payload) {
        this.payload = payload;
    }

    @Override
    public MultiPoint getPayload() {
        return this.payload;
    }

    @Override
    public Class<MultiPoint> getSupportedClass() {
        return MultiPoint.class;
    }
}
