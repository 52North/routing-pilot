package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * ProcessOffering
 */
@Validated
public class ProcessOffering {
    @JsonProperty("process")
    private Process process = null;

    @NotNull
    @Valid
    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessOffering that = (ProcessOffering) o;
        return Objects.equals(this.getProcess(), that.getProcess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProcess());
    }

}
