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
package org.n52.testbed.routing;

import org.locationtech.jts.geom.Point;
import org.n52.testbed.routing.model.routing.Preference;
import org.n52.testbed.routing.model.routing.Route;

import java.util.Objects;

public abstract class AbstractRoutingAlgorithm implements RoutingAlgorithm {
    private Route route;
    private String name;
    private Point startPoint;
    private Point endPoint;
    private Preference preference;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    protected String getName() {
        return name;
    }

    @Override
    public void setStartPoint(Point startPoint) {
        this.startPoint = Objects.requireNonNull(startPoint);
    }

    protected Point getStartPoint() {
        return startPoint;
    }

    @Override
    public void setEndPoint(Point endPoint) {
        this.endPoint = Objects.requireNonNull(endPoint);
    }

    protected Point getEndPoint() {
        return endPoint;
    }

    @Override
    public void setPreference(Preference preference) {
        this.preference = Objects.requireNonNull(preference);
    }

    protected Preference getPreference() {
        return preference;
    }

    @Override
    public void execute() throws Exception {
        this.route = computeRoute();
    }

    @Override
    public Route getRoute() {
        return this.route;
    }

    protected abstract Route computeRoute() throws Exception;

}
