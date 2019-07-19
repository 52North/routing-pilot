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
import com.fasterxml.jackson.annotation.JsonProperty;
import org.n52.testbed.routing.model.GeoJsonType;
import org.n52.testbed.routing.model.Link;
import org.n52.testbed.routing.model.wps.Status;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The route is represented as a GeoJSON feature collection.
 */
@Validated
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Route {

    @JsonProperty("type")
    private final GeoJsonType type = GeoJsonType.FEATURE_COLLECTION;

    @JsonProperty("status")
    private Status status = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("links")
    @Valid
    private List<Link> links = null;

    @JsonProperty("features")
    @Valid
    @JsonInclude
    private List<RouteFeature> features = new ArrayList<>();

    @JsonProperty("bbox")
    @Valid
    private List<BigDecimal> bbox = null;

    @NotNull
    @Valid
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Valid
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @NotNull
    public List<RouteFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<RouteFeature> features) {
        this.features = features;
    }

    @Valid
    @Size(min = 4)
    public List<BigDecimal> getBbox() {
        return bbox;
    }

    public void setBbox(List<BigDecimal> bbox) {
        this.bbox = bbox;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Route route = (Route) o;
        return Objects.equals(this.status, route.status) &&
                Objects.equals(this.name, route.name) &&
                Objects.equals(this.links, route.links) &&
                Objects.equals(this.features, route.features) &&
                Objects.equals(this.bbox, route.bbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, status, name, links, features, bbox);
    }
}
