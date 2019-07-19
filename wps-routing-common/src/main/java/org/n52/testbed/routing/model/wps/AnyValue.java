package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * AnyValue
 */
@Validated
public class AnyValue implements ValueDefinition {
    @JsonProperty("anyValue")
    private Boolean anyValue = true;

    public Boolean isAnyValue() {
        return anyValue;
    }

    public void setAnyValue(Boolean anyValue) {
        this.anyValue = anyValue;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnyValue that = (AnyValue) o;
        return Objects.equals(this.isAnyValue(), that.isAnyValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAnyValue());
    }

}
