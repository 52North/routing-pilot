package org.n52.testbed.routing.io.geojson;

import org.locationtech.jts.geom.LineString;

public class LineStringData implements GeoJsonData<LineString> {
    private final LineString payload;

    public LineStringData(LineString payload) {
        this.payload = payload;
    }

    @Override
    public LineString getPayload() {
        return this.payload;
    }

    @Override
    public Class<LineString> getSupportedClass() {
        return LineString.class;
    }
}
