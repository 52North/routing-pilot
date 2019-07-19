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
package org.n52.testbed.routing.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.locationtech.jts.geom.Geometry;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Feature
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Feature<F extends Feature<F>> implements Serializable {
    private static final long serialVersionUID = 2368303759089693613L;
    @JsonProperty("id")
    private Object id;
    @JsonProperty("type")
    private final GeoJsonType type = GeoJsonType.FEATURE;
    @JsonProperty("properties")
    @JsonInclude
    private Map<String, Object> properties;
    @JsonProperty("geometry")
    private Geometry geometry;
    @JsonProperty("links")
    @Valid
    private List<Link> links;

    public Feature() {
        this(null, null, null, null);
    }

    public Feature(Geometry geometry) {
        this(null, geometry, null, null);
    }

    public Feature(Geometry geometry, Map<String, Object> properties) {
        this(null, geometry, properties);
    }

    public Feature(Object id, Geometry geometry, Map<String, Object> properties) {
        this(id, geometry, properties, null);
    }

    public Feature(Object id, Geometry geometry, Map<String, Object> properties, List<Link> links) {
        this.id = id;
        this.properties = properties;
        this.geometry = geometry;
        this.links = links;
    }

    @SuppressWarnings("unchecked")
    protected F self() {
        return (F) this;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public F id(Object id) {
        this.id = id;
        return self();
    }

    public F links(List<Link> links) {
        this.links = links;
        return self();
    }

    public F addLinksItem(Link linksItem) {
        this.links.add(linksItem);
        return self();
    }

    @NotNull
    @Valid
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @NotNull
    public GeoJsonType getType() {
        return type;
    }

    public F properties(Map<String, Object> properties) {
        this.properties = properties;
        return self();
    }

    @NotNull
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public F geometry(Geometry geometry) {
        this.geometry = geometry;
        return self();
    }

    @NotNull
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        F feature = (F) o;
        return Objects.equals(this.type, feature.getType()) &&
                Objects.equals(this.properties, feature.getProperties()) &&
                Objects.equals(this.geometry, feature.getGeometry()) &&
                Objects.equals(this.getLinks(), feature.getLinks());

    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getProperties(), getGeometry(), getLinks());
    }

}

