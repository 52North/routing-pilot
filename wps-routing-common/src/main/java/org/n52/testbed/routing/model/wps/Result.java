package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Result
 */
@Validated
public class Result {
    @JsonProperty("outputs")
    @Valid
    private List<ResultOutputs> outputs = new ArrayList<>();

    @NotNull
    @Valid
    public List<ResultOutputs> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<ResultOutputs> outputs) {
        this.outputs = outputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Result that = (Result) o;
        return Objects.equals(this.getOutputs(), that.getOutputs());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOutputs());
    }
}
