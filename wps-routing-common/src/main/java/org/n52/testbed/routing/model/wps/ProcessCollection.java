package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ProcessCollection
 */
@Validated
public class ProcessCollection {
    @JsonProperty("processes")
    @Valid
    private List<ProcessSummary> processes = new ArrayList<>();

    @NotNull
    @Valid
    public List<ProcessSummary> getProcesses() {
        return processes;
    }

    public void setProcesses(List<ProcessSummary> processes) {
        this.processes = processes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessCollection that = (ProcessCollection) o;
        return Objects.equals(this.getProcesses(), that.getProcesses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProcesses());
    }

}
