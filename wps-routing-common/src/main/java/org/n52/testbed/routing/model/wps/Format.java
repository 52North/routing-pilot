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

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Format
 */
@Validated
public class Format {
    @JsonProperty("mimeType")
    private String mimeType;

    @JsonProperty("schema")
    private String schema;

    @JsonProperty("encoding")
    private String encoding;

    public Format(String mimeType, String schema) {
        this(mimeType, schema, null);
    }

    public Format(String mimeType) {
        this(mimeType, null, null);
    }

    public Format(String mimeType, String schema, String encoding) {
        this.mimeType = mimeType;
        this.schema = schema;
        this.encoding = encoding;
    }

    public Format() {
        this(null, null, null);
    }

    @NotNull
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Format that = (Format) o;
        return Objects.equals(this.getMimeType(), that.getMimeType()) &&
                Objects.equals(this.getSchema(), that.getSchema()) &&
                Objects.equals(this.getEncoding(), that.getEncoding());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMimeType(), getSchema(), getEncoding());
    }
}
