package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * LiteralData
 */
@Validated
public class LiteralData implements Data {
    @JsonProperty("value")
    private String value;

    @JsonProperty("dataType")
    private NamedReference dataType;

    @JsonProperty("uom")
    private NamedReference uom;

    public LiteralData() {
        this(null, null, null);
    }

    public LiteralData(String value, NamedReference uom, NamedReference dataType) {
        this.value = value;
        this.dataType = dataType;
        this.uom = uom;
    }

    public LiteralData(String value, NamedReference uom) {
        this(value, uom, null);
    }

    public LiteralData(String value) {
        this(value, null, null);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Valid
    public NamedReference getDataType() {
        return dataType;
    }

    public void setDataType(NamedReference dataType) {
        this.dataType = dataType;
    }

    @Valid
    public NamedReference getUom() {
        return uom;
    }

    public void setUom(NamedReference uom) {
        this.uom = uom;
    }

    public LiteralData uom(NamedReference uom) {
        this.uom = uom;
        return this;
    }

    @Override
    public boolean isLiteral() {
        return true;
    }

    @Override
    public LiteralData asLiteral() {
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
        LiteralData that = (LiteralData) o;
        return Objects.equals(this.getValue(), that.getValue()) &&
                Objects.equals(this.getDataType(), that.getDataType()) &&
                Objects.equals(this.getUom(), that.getUom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue(), getDataType(), getUom());
    }
}
