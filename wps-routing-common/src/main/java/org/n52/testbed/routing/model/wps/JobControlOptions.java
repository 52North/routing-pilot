package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Gets or Sets jobControlOptions
 */
public enum JobControlOptions {
    SYNC_EXECUTE("sync-execute"),
    ASYNC_EXECUTE("async-execute");

    private String value;

    JobControlOptions(String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static JobControlOptions fromValue(String text) {
        for (JobControlOptions jobControlOptions : JobControlOptions.values()) {
            if (jobControlOptions.value.equals(text)) {
                return jobControlOptions;
            }
        }
        return null;
    }
}
