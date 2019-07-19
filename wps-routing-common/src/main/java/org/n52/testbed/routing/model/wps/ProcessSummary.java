/*
 * Copyright 2019 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.n52.testbed.routing.model.Link;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * ProcessSummary
 */
@Validated
public class ProcessSummary extends DescriptionType {
    @JsonProperty("version")
    private String version = null;

    @JsonProperty("jobControlOptions")
    @Valid
    private List<JobControlOptions> jobControlOptions = null;

    @JsonProperty("outputTransmission")
    @Valid
    private List<TransmissionMode> outputTransmission = null;

    @JsonProperty("links")
    @Valid
    private List<Link> links = null;

    @NotNull
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Valid
    public List<JobControlOptions> getJobControlOptions() {
        return jobControlOptions;
    }

    public void setJobControlOptions(List<JobControlOptions> jobControlOptions) {
        this.jobControlOptions = jobControlOptions;
    }

    @Valid
    public List<TransmissionMode> getOutputTransmission() {
        return outputTransmission;
    }

    public void setOutputTransmission(List<TransmissionMode> outputTransmission) {
        this.outputTransmission = outputTransmission;
    }

    @Valid
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessSummary processSummary = (ProcessSummary) o;
        return Objects.equals(this.getVersion(), processSummary.getVersion()) &&
                Objects.equals(this.getJobControlOptions(), processSummary.getJobControlOptions()) &&
                Objects.equals(this.getOutputTransmission(), processSummary.getOutputTransmission()) &&
                Objects.equals(this.getLinks(), processSummary.getLinks()) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVersion(), getJobControlOptions(), getOutputTransmission(), getLinks(), super.hashCode());
    }

}
