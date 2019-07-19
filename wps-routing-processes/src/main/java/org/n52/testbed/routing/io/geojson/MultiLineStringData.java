package org.n52.testbed.routing.io.geojson;

import org.locationtech.jts.geom.MultiLineString;

public class MultiLineStringData implements GeoJsonData<MultiLineString> {
    private final MultiLineString payload;

    public MultiLineStringData(MultiLineString payload) {
        this.payload = payload;
    }

    @Override
    public MultiLineString getPayload() {
        return this.payload;
    }

    @Override
    public Class<MultiLineString> getSupportedClass() {
        return MultiLineString.class;
    }
}
