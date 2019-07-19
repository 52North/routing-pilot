package org.n52.testbed.routing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * This value is fixed and identifies this object as a GeoJSON feature collection.
 */
public enum GeoJsonType {
    FEATURE("Feature"),
    FEATURE_COLLECTION("FeatureCollection");


    private String value;

    GeoJsonType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static GeoJsonType fromValue(String text) {
        for (GeoJsonType b : GeoJsonType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException(String.format("Unexpected value '%s'", text));
    }
}
