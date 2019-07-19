package org.n52.testbed.routing.io.geojson;

import org.locationtech.jts.geom.Point;

public class PointData implements GeoJsonData<Point> {
    private final Point payload;

    public PointData(Point payload) {
        this.payload = payload;
    }

    @Override
    public Point getPayload() {
        return this.payload;
    }

    @Override
    public Class<Point> getSupportedClass() {
        return Point.class;
    }
}
