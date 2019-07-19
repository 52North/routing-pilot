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

import java.util.Objects;

/**
 * Range
 */
@Validated
public class Range {
    @JsonProperty("minimumValue")
    private String minimumValue;

    @JsonProperty("maximumValue")
    private String maximumValue;

    @JsonProperty("spacing")
    private String spacing;

    @JsonProperty("rangeClosure")
    private RangeClosure rangeClosure;

    public String getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(String minimumValue) {
        this.minimumValue = minimumValue;
    }

    public String getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(String maximumValue) {
        this.maximumValue = maximumValue;
    }

    public String getSpacing() {
        return spacing;
    }

    public void setSpacing(String spacing) {
        this.spacing = spacing;
    }

    public RangeClosure getRangeClosure() {
        return rangeClosure;
    }

    public void setRangeClosure(RangeClosure rangeClosure) {
        this.rangeClosure = rangeClosure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Range that = (Range) o;
        return Objects.equals(this.getMinimumValue(), that.getMinimumValue()) &&
                Objects.equals(this.getMaximumValue(), that.getMaximumValue()) &&
                Objects.equals(this.getSpacing(), that.getSpacing()) &&
                Objects.equals(this.getRangeClosure(), that.getRangeClosure());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMinimumValue(), getMaximumValue(), getSpacing(), getRangeClosure());
    }
}
