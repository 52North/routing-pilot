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
package org.n52.testbed.routing.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.validation.annotation.Validated;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

/**
 * RouteStartOrEndProperties
 */
@Validated
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteStartOrEndProperties extends RouteFeatureProperties<RouteStartOrEndProperties> {

    @NotNull
    private RouteFeatureType type;

    public RouteStartOrEndProperties() {
        this(null);
    }

    public RouteStartOrEndProperties(@NotNull RouteFeatureType type) {
        this.type = type;
    }

    @JsonSetter()
    public void setType(RouteFeatureType type) {
        if (type != RouteFeatureType.START && type != RouteFeatureType.END) {
            throw new ValidationException();
        }
        this.type = type;
    }

    public RouteStartOrEndProperties type(RouteFeatureType type) {
        setType(type);
        return self();
    }

    @Override
    public RouteFeatureType getType() {
        return this.type;
    }
}
