package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.n52.testbed.routing.model.routing.Route;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * ResultValue
 */
@Validated
public class ResultValue {
    @JsonProperty("inlineValue")
    private Route inlineValue = null;

    @Valid
    public Route getInlineValue() {
        return inlineValue;
    }

    public void setInlineValue(Route inlineValue) {
        this.inlineValue = inlineValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResultValue that = (ResultValue) o;
        return Objects.equals(this.getInlineValue(), that.getInlineValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInlineValue());
    }

}
