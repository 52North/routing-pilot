package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Process
 */
@Validated
public class Process extends ProcessSummary {
    @JsonProperty("inputs")
    @Valid
    private List<InputDescription> inputs = new ArrayList<>();

    @JsonProperty("outputs")
    @Valid
    private List<OutputDescription> outputs = new ArrayList<>();

    @NotNull
    @Valid
    public List<InputDescription> getInputs() {
        return inputs;
    }

    public void setInputs(List<InputDescription> inputs) {
        this.inputs = inputs;
    }

    @NotNull
    @Valid
    public List<OutputDescription> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<OutputDescription> outputs) {
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
        Process that = (Process) o;
        return Objects.equals(this.getInputs(), that.getInputs()) &&
                Objects.equals(this.getOutputs(), that.getOutputs()) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInputs(), getOutputs(), super.hashCode());
    }

}
