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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * The time of departure or arrival. The default value is now (departure).  Support for this parameter is not required
 * and the parameter may be removed from the API definition, if conformance class **&#x27;time&#x27;** is not listed in
 * the conformance declaration under &#x60;/conformance&#x60;.
 */
@Validated
public class When implements Serializable {
    private static final long serialVersionUID = -5246276867833136226L;
    @JsonProperty("timestamp")
    private OffsetDateTime timestamp;

    @JsonProperty("type")
    private Type type;

    public When() {
        this(Type.DEPARTURE, null);
    }

    public When(Type type, OffsetDateTime timestamp) {
        this.timestamp = timestamp;
        this.type = type;
    }

    @NotNull
    @Valid
    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof When)) {
            return false;
        }
        When when = (When) o;
        return Objects.equals(getTimestamp(), when.getTimestamp()) &&
               getType() == when.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimestamp(), getType());
    }

    public enum Type {
        DEPARTURE("departure"),
        ARRIVAL("arrival");

        private String value;

        Type(@NotNull String value) {
            this.value = Objects.requireNonNull(value);
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static Type fromValue(String text) {
            for (Type type : Type.values()) {
                if (type.value.equals(text)) {
                    return type;
                }
            }
            return null;
        }
    }

}
