package org.n52.testbed.routing.model.routing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * the local unit used for speed limits
 */
public enum SpeedLimitUnit {
    KMPH("kmph"),
    MPH("mph");

    private String value;

    SpeedLimitUnit(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static SpeedLimitUnit fromValue(String text) {
        for (SpeedLimitUnit b : SpeedLimitUnit.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
