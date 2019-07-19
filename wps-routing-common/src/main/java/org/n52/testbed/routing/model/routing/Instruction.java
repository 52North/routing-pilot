package org.n52.testbed.routing.model.routing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * instructions at the end of the segment (continue, turn right or turn left to a different street)
 */
public enum Instruction {
    CONTINUE("continue"),
    LEFT("left"),
    RIGHT("right");

    private String value;

    Instruction(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Instruction fromValue(String text) {
        for (Instruction b : Instruction.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
