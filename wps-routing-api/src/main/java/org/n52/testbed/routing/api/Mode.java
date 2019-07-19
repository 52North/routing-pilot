package org.n52.testbed.routing.api;

import java.util.Optional;

public enum Mode {
    SYNC("sync"),
    ASYNC("async");

    private final String value;

    Mode(String value) {
        this.value = value;
    }

    public static Optional<Mode> fromString(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        for (Mode mode : values()) {
            if (mode.value.equals(value)) {
                return Optional.of(mode);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return this.value;
    }
}
