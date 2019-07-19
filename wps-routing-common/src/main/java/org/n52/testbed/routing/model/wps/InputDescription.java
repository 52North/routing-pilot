package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * InputDescription
 */
@Validated
public class InputDescription extends DescriptionType {
    @JsonProperty("input")
    private DataTypeDescription input = null;

    @JsonProperty("minOccurs")
    private Integer minOccurs = null;

    @JsonProperty("maxOccurs")
    private Integer maxOccurs = null;

    public DataTypeDescription getInput() {
        return input;
    }

    public void setInput(DataTypeDescription input) {
        this.input = input;
    }

    public Integer getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(Integer minOccurs) {
        this.minOccurs = minOccurs;
    }

    public Integer getMaxOccurs() {
        return maxOccurs;
    }

    public void setMaxOccurs(Integer maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InputDescription that = (InputDescription) o;
        return Objects.equals(this.getInput(), that.getInput()) &&
                Objects.equals(this.getMinOccurs(), that.getMinOccurs()) &&
                Objects.equals(this.getMaxOccurs(), that.getMaxOccurs()) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInput(), getMinOccurs(), getMaxOccurs(), super.hashCode());
    }

}
