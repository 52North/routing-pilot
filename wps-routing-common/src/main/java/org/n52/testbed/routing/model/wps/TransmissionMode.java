package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Gets or Sets transmissionMode
 */
public enum TransmissionMode {
    VALUE("value"),
    REFERENCE("reference");

    private String value;

    TransmissionMode(String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @JsonCreator
    public static TransmissionMode fromValue(String text) {
        for (TransmissionMode transmissionMode : TransmissionMode.values()) {
            if (transmissionMode.value.equals(text)) {
                return transmissionMode;
            }
        }
        return null;
    }
}
