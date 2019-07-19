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
package org.n52.testbed.routing.api;

import java.util.Optional;

public enum Mode {
    SYNC("sync"),
    ASYNC("async");

    private final String value;

    Mode(String value) {
        this.value = value;
    }

    public static Optional<Mode> fromString(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        for (Mode mode : values()) {
            if (mode.value.equals(value)) {
                return Optional.of(mode);
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return this.value;
    }
}
