package org.n52.testbed.routing.model.routing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The routing preference.  Every API has to support at least 'fastest' and 'shortest'. The default value should be 'fastest'.
 */
public enum Preference {
    FASTEST("fastest"),

    SHORTEST("shortest");

    private String value;

    Preference(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Preference fromValue(String text) {
        for (Preference b : Preference.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
