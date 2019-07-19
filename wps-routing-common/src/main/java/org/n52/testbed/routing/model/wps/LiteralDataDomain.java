package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * LiteralDataDomain
 */
@Validated
public class LiteralDataDomain {
    @JsonProperty("defaultValue")
    private String defaultValue;

    @JsonProperty("dataType")
    private NamedReference dataType;

    @JsonProperty("uom")
    private NamedReference uom;

    @JsonProperty("valueDefinition")
    private ValueDefinition valueDefinition;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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

    @Valid
    public ValueDefinition getValueDefinition() {
        return valueDefinition;
    }

    public void setValueDefinition(ValueDefinition valueDefinition) {
        this.valueDefinition = valueDefinition;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LiteralDataDomain that = (LiteralDataDomain) o;
        return Objects.equals(this.getDefaultValue(), that.getDefaultValue()) &&
                Objects.equals(this.getDataType(), that.getDataType()) &&
                Objects.equals(this.getUom(), that.getUom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDefaultValue(), getDataType(), getUom());
    }

}
