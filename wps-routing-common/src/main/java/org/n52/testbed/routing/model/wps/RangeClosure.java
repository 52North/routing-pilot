package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Gets or Sets rangeClosure
 */
public enum RangeClosure {
    CLOSED("closed"),
    CLOSED_OPEN("closed-open"),
    OPEN("open"),
    OPEN_CLOSED("open-closed");

    private String value;

    RangeClosure(String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RangeClosure fromValue(String text) {
        for (RangeClosure rangeClosure : RangeClosure.values()) {
            if (rangeClosure.value.equals(text)) {
                return rangeClosure;
            }
        }
        return null;
    }
}
