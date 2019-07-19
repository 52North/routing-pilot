package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * FormatDescription
 */
@Validated
public class FormatDescription {
    @JsonUnwrapped
    private Format format;
    @JsonProperty("maximumMegabytes")
    private Integer maximumMegabytes = null;
    @JsonProperty("default")
    private Boolean defaultValue = false;

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getMaximumMegabytes() {
        return maximumMegabytes;
    }

    public void setMaximumMegabytes(Integer maximumMegabytes) {
        this.maximumMegabytes = maximumMegabytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormatDescription that = (FormatDescription) o;
        return Objects.equals(this.getMaximumMegabytes(), that.getMaximumMegabytes()) &&
                Objects.equals(this.getDefaultValue(), that.getDefaultValue()) &&
                Objects.equals(this.getFormat(), that.getFormat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaximumMegabytes(), getDefaultValue(), getFormat());
    }

}
