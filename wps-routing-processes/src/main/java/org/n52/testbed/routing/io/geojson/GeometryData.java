package org.n52.testbed.routing.io.geojson;

import org.locationtech.jts.geom.Geometry;

public class GeometryData implements GeoJsonData<Geometry> {
    private final Geometry payload;

    public GeometryData(Geometry payload) {
        this.payload = payload;
    }

    @Override
    public Geometry getPayload() {
        return this.payload;
    }

    @Override
    public Class<Geometry> getSupportedClass() {
        return Geometry.class;
    }
}
