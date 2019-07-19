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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.n52.testbed.routing.model.FeatureProperties;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RouteOverviewProperties.class, name = "overview"),
        @JsonSubTypes.Type(value = RouteSegmentProperties.class, name = "segment"),
        @JsonSubTypes.Type(value = RouteStartOrEndProperties.class, name = "start"),
        @JsonSubTypes.Type(value = RouteStartOrEndProperties.class, name = "end")
})
public abstract class RouteFeatureProperties<T extends RouteFeatureProperties<T>> extends FeatureProperties<T> {

    public static final String TYPE = "type";

    @NotNull
    @JsonProperty(TYPE)
    public abstract RouteFeatureType getType();

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        T properties = (T) o;
        return Objects.equals(getType(), properties.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }

}
