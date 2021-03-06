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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RouteFeatureType {
    END("end"),
    OVERVIEW("overview"),
    SEGMENT("segment"),
    START("start");

    private String value;

    RouteFeatureType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RouteFeatureType fromValue(String text) {
        for (RouteFeatureType routeFeatureType : RouteFeatureType.values()) {
            if (String.valueOf(routeFeatureType.value).equals(text)) {
                return routeFeatureType;
            }
        }
        return null;
    }
}
