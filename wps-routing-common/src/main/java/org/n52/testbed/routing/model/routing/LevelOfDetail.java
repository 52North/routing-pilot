package org.n52.testbed.routing.model.routing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ready to visualize (with all the coordinates that allow visualization of the shape of the segment) or summary (with just start and end coordinates of the segment)
 */
public enum LevelOfDetail {
    SUMMARY("summary"),
    VISUALIZATION("visualization");

    private String value;

    LevelOfDetail(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static LevelOfDetail fromValue(String text) {
        for (LevelOfDetail b : LevelOfDetail.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
