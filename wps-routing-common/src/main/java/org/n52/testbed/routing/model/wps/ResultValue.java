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
import org.n52.testbed.routing.model.routing.Route;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * ResultValue
 */
@Validated
public class ResultValue {
    @JsonProperty("inlineValue")
    private Route inlineValue = null;

    @Valid
    public Route getInlineValue() {
        return inlineValue;
    }

    public void setInlineValue(Route inlineValue) {
        this.inlineValue = inlineValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResultValue that = (ResultValue) o;
        return Objects.equals(this.getInlineValue(), that.getInlineValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInlineValue());
    }

}
