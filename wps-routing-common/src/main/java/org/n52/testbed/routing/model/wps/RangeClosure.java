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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * Gets or Sets rangeClosure
 */
public enum RangeClosure {
    CLOSED("closed"),
    CLOSED_OPEN("closed-open"),
    OPEN("open"),
    OPEN_CLOSED("open-closed");

    private String value;

    RangeClosure(String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RangeClosure fromValue(String text) {
        for (RangeClosure rangeClosure : RangeClosure.values()) {
            if (rangeClosure.value.equals(text)) {
                return rangeClosure;
            }
        }
        return null;
    }
}
