package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * JobCollection
 */
@Validated
public class JobCollection {
    @JsonProperty("jobs")
    @Valid
    private List<String> jobs = new ArrayList<>();

    @NotNull
    public List<String> getJobs() {
        return jobs;
    }

    public void setJobs(List<String> jobs) {
        this.jobs = jobs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobCollection that = (JobCollection) o;
        return Objects.equals(this.getJobs(), that.getJobs());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJobs());
    }

}
