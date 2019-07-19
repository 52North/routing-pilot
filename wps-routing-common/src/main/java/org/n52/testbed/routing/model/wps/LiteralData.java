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

import javax.validation.Valid;
import java.util.Objects;

/**
 * LiteralData
 */
@Validated
public class LiteralData implements Data {
    @JsonProperty("value")
    private String value;

    @JsonProperty("dataType")
    private NamedReference dataType;

    @JsonProperty("uom")
    private NamedReference uom;

    public LiteralData() {
        this(null, null, null);
    }

    public LiteralData(String value, NamedReference uom, NamedReference dataType) {
        this.value = value;
        this.dataType = dataType;
        this.uom = uom;
    }

    public LiteralData(String value, NamedReference uom) {
        this(value, uom, null);
    }

    public LiteralData(String value) {
        this(value, null, null);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Valid
    public NamedReference getDataType() {
        return dataType;
    }

    public void setDataType(NamedReference dataType) {
        this.dataType = dataType;
    }

    @Valid
    public NamedReference getUom() {
        return uom;
    }

    public void setUom(NamedReference uom) {
        this.uom = uom;
    }

    public LiteralData uom(NamedReference uom) {
        this.uom = uom;
        return this;
    }

    @Override
    public boolean isLiteral() {
        return true;
    }

    @Override
    public LiteralData asLiteral() {
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LiteralData that = (LiteralData) o;
        return Objects.equals(this.getValue(), that.getValue()) &&
                Objects.equals(this.getDataType(), that.getDataType()) &&
                Objects.equals(this.getUom(), that.getUom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue(), getDataType(), getUom());
    }
}
