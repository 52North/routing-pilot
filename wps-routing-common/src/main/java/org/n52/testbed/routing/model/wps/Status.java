package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * The processing status.
 */
public enum Status {
    ACCEPTED("accepted"),
    RUNNING("running"),
    SUCCESSFUL("successful"),
    FAILED("failed");

    private String value;

    Status(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public boolean isFinished() {
        return isSuccess() || isFailure();
    }

    public boolean isSuccess() {
        return this == SUCCESSFUL;
    }

    public boolean isFailure() {
        return this == FAILED;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Status fromValue(String text) {
        for (Status status : Status.values()) {
            if (status.value.equals(text)) {
                return status;
            }
        }
        return null;
    }
}
