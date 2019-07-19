package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Range
 */
@Validated
public class Range {
    @JsonProperty("minimumValue")
    private String minimumValue = null;

    @JsonProperty("maximumValue")
    private String maximumValue = null;

    @JsonProperty("spacing")
    private String spacing = null;

    @JsonProperty("rangeClosure")
    private RangeClosure rangeClosure = null;

    public String getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(String minimumValue) {
        this.minimumValue = minimumValue;
    }

    public String getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(String maximumValue) {
        this.maximumValue = maximumValue;
    }

    public String getSpacing() {
        return spacing;
    }

    public void setSpacing(String spacing) {
        this.spacing = spacing;
    }

    public RangeClosure getRangeClosure() {
        return rangeClosure;
    }

    public void setRangeClosure(RangeClosure rangeClosure) {
        this.rangeClosure = rangeClosure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Range that = (Range) o;
        return Objects.equals(this.getMinimumValue(), that.getMinimumValue()) &&
                Objects.equals(this.getMaximumValue(), that.getMaximumValue()) &&
                Objects.equals(this.getSpacing(), that.getSpacing()) &&
                Objects.equals(this.getRangeClosure(), that.getRangeClosure());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMinimumValue(), getMaximumValue(), getSpacing(), getRangeClosure());
    }
}
