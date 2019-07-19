package org.n52.testbed.routing.model.routing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RouteFeatureType {
    END("end"),
    OVERVIEW("overview"),
    SEGMENT("segment"),
    START("start");


    private String value;

    RouteFeatureType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RouteFeatureType fromValue(String text) {
        for (RouteFeatureType routeFeatureType : RouteFeatureType.values()) {
            if (String.valueOf(routeFeatureType.value).equals(text)) {
                return routeFeatureType;
            }
        }
        return null;
    }
}
