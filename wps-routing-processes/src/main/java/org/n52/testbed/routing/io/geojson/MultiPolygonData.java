package org.n52.testbed.routing.io.geojson;

import org.locationtech.jts.geom.MultiPolygon;

public class MultiPolygonData implements GeoJsonData<MultiPolygon> {
    private final MultiPolygon payload;

    public MultiPolygonData(MultiPolygon payload) {
        this.payload = payload;
    }

    @Override
    public MultiPolygon getPayload() {
        return this.payload;
    }

    @Override
    public Class<MultiPolygon> getSupportedClass() {
        return MultiPolygon.class;
    }
}
