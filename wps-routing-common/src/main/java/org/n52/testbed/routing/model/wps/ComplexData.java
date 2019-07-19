package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * ComplexData
 */
@Validated
public class ComplexData implements Data {
    @JsonProperty("value")
    private Object value;
    @JsonProperty("format")
    private Format format;

    public ComplexData(Format format) {
        this(null, format);
    }

    public ComplexData(Object value) {
        this(value, null);
    }

    public ComplexData() {
        this(null, null);
    }

    public ComplexData(Object value, Format format) {
        this.value = value;
        this.format = format;
    }

    @Valid
    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    @NotNull
    @Valid
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public ComplexData asComplex() {
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexData that = (ComplexData) o;
        return Objects.equals(this.getValue(), that.getValue()) &&
                Objects.equals(this.getFormat(), that.getFormat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue(), getFormat());
    }

}
