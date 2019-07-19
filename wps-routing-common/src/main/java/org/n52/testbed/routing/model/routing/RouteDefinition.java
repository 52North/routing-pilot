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
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Objects;

/**
 * The definition of a route. At a minimum, a route is defined by a start and end point.  More parameters and routing constraints will be added later.
 */
@Validated
public class RouteDefinition {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("waypoints")
    private MultiPoint waypoints = null;

    @JsonProperty("preference")
    private Preference preference = Preference.FASTEST;

    @JsonProperty("maxHeight")
    private BigDecimal maxHeight = null;

    @JsonProperty("maxWeight")
    private BigDecimal maxWeight = null;

    @JsonProperty("obstacles")
    private MultiPolygon obstacles = null;

    @JsonProperty("dataset")
    private String dataset = null;

    @JsonProperty("engine")
    private String engine = null;

    @JsonProperty("algorithm")
    private String algorithm = null;

    @JsonProperty("when")
    private When when = null;

    @JsonProperty("subscriber")
    private URI subscriber = null;

    public RouteDefinition name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RouteDefinition waypoints(MultiPoint waypoints) {
        this.waypoints = waypoints;
        return this;
    }

    @NotNull
    @Valid
    public MultiPoint getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(MultiPoint waypoints) {
        this.waypoints = waypoints;
    }

    public RouteDefinition preference(Preference preference) {
        this.preference = preference;
        return this;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public RouteDefinition maxHeight(BigDecimal maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    @Valid
    public BigDecimal getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(BigDecimal maxHeight) {
        this.maxHeight = maxHeight;
    }

    public RouteDefinition maxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
        return this;
    }

    @Valid
    public BigDecimal getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
    }

    public RouteDefinition obstacles(MultiPolygon obstacles) {
        this.obstacles = obstacles;
        return this;
    }

    @Valid
    public MultiPolygon getObstacles() {
        return obstacles;
    }

    public void setObstacles(MultiPolygon obstacles) {
        this.obstacles = obstacles;
    }

    public RouteDefinition dataset(String dataset) {
        this.dataset = dataset;
        return this;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public RouteDefinition engine(String engine) {
        this.engine = engine;
        return this;
    }


    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public RouteDefinition algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public RouteDefinition when(When when) {
        this.when = when;
        return this;
    }

    @Valid
    public When getWhen() {
        return when;
    }

    public void setWhen(When when) {
        this.when = when;
    }

    public RouteDefinition subscriber(URI subscriber) {
        this.subscriber = subscriber;
        return this;
    }

    public URI getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(URI subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RouteDefinition routeDefinition = (RouteDefinition) o;
        return Objects.equals(this.name, routeDefinition.name) &&
                Objects.equals(this.waypoints, routeDefinition.waypoints) &&
                Objects.equals(this.preference, routeDefinition.preference) &&
                Objects.equals(this.maxHeight, routeDefinition.maxHeight) &&
                Objects.equals(this.maxWeight, routeDefinition.maxWeight) &&
                Objects.equals(this.obstacles, routeDefinition.obstacles) &&
                Objects.equals(this.dataset, routeDefinition.dataset) &&
                Objects.equals(this.engine, routeDefinition.engine) &&
                Objects.equals(this.algorithm, routeDefinition.algorithm) &&
                Objects.equals(this.when, routeDefinition.when) &&
                Objects.equals(this.subscriber, routeDefinition.subscriber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, waypoints, preference, maxHeight, maxWeight, obstacles, dataset, engine, algorithm, when, subscriber);
    }
}
