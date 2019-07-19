package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * ResultOutputs
 */
@Validated
public class ResultOutputs {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("value")
    private ResultValue value = null;

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull
    @Valid
    public ResultValue getValue() {
        return value;
    }

    public void setValue(ResultValue value) {
        this.value = value;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResultOutputs resultOutputs = (ResultOutputs) o;
        return Objects.equals(this.getId(), resultOutputs.getId()) &&
                Objects.equals(this.getValue(), resultOutputs.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getValue());
    }
}
