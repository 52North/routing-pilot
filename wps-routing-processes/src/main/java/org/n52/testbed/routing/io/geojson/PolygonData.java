package org.n52.testbed.routing.io.geojson;

import org.locationtech.jts.geom.Polygon;

public class PolygonData implements GeoJsonData<Polygon> {
    private final Polygon payload;

    public PolygonData(Polygon payload) {
        this.payload = payload;
    }

    @Override
    public Polygon getPayload() {
        return this.payload;
    }

    @Override
    public Class<Polygon> getSupportedClass() {
        return Polygon.class;
    }
}
