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
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * FormatDescription
 */
@Validated
public class FormatDescription {
    @JsonUnwrapped
    private Format format;
    @JsonProperty("maximumMegabytes")
    private Integer maximumMegabytes = null;
    @JsonProperty("default")
    private Boolean defaultValue = false;

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getMaximumMegabytes() {
        return maximumMegabytes;
    }

    public void setMaximumMegabytes(Integer maximumMegabytes) {
        this.maximumMegabytes = maximumMegabytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormatDescription that = (FormatDescription) o;
        return Objects.equals(this.getMaximumMegabytes(), that.getMaximumMegabytes()) &&
                Objects.equals(this.getDefaultValue(), that.getDefaultValue()) &&
                Objects.equals(this.getFormat(), that.getFormat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaximumMegabytes(), getDefaultValue(), getFormat());
    }

}
