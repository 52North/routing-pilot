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
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * Execute
 */
@Validated
public class Execute {
    @JsonProperty("inputs")
    @Valid
    private List<Input> inputs;

    @JsonProperty("outputs")
    @Valid
    private List<Output> outputs;

    public Execute(@Valid List<Input> inputs, @Valid List<Output> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public Execute() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    @Valid
    public List<Input> getInputs() {
        return inputs;
    }

    public Map<String, Input> getInputMap() {
        return getInputs().stream().collect(toMap(Input::getId, identity()));
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    @NotNull
    @Valid
    public List<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Execute that = (Execute) o;
        return Objects.equals(this.getInputs(), that.getInputs()) &&
                Objects.equals(this.getOutputs(), that.getOutputs());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInputs(), getOutputs());
    }

}
