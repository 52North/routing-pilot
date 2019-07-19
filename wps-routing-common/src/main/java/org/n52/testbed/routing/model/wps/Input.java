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

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Input
 */
@Validated
public class Input {
    @JsonProperty("id")
    private String id;

    @JsonProperty("input")
    private Data input;

    public Input() {
    }

    public Input(String id) {
        this(id, null);
    }

    public Input(String id, Data input) {
        this.id = id;
        this.input = input;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull
    public Data getInput() {
        return input;
    }

    public void setInput(Data input) {
        this.input = input;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Input that = (Input) o;
        return Objects.equals(this.getId(), that.getId()) &&
                Objects.equals(this.getInput(), that.getInput());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getInput());
    }

}
