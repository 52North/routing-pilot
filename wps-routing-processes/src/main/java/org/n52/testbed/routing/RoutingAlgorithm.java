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

import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.n52.javaps.algorithm.annotation.ComplexInput;
import org.n52.javaps.algorithm.annotation.ComplexOutput;
import org.n52.javaps.algorithm.annotation.Execute;
import org.n52.javaps.algorithm.annotation.LiteralInput;
import org.n52.javaps.io.literal.xsd.LiteralStringType;
import org.n52.testbed.routing.io.RouteData;
import org.n52.testbed.routing.io.geojson.PointData;
import org.n52.testbed.routing.model.routing.Preference;
import org.n52.testbed.routing.model.routing.Route;
import org.n52.testbed.routing.model.wps.Inputs;
import org.n52.testbed.routing.model.wps.Outputs;

import java.util.Optional;

public interface RoutingAlgorithm {
    @ComplexInput(identifier = Inputs.START, title = "Start Point", abstrakt = "The starting point of the route.",
                  binding = PointData.class)
    void setStartPoint(Point startPoint);

    @ComplexInput(identifier = Inputs.END, title = "End point", abstrakt = "The end point of the route.",
                  binding = PointData.class)
    void setEndPoint(Point endPoint);

    @LiteralInput(identifier = Inputs.NAME, title = "The name", abstrakt = "The name of the route.", minOccurs = 0,
                  maxOccurs = 1, binding = LiteralStringType.class)
    void setName(String name);

    @LiteralInput(identifier = Inputs.PREFERENCE, title = "Preference", abstrakt = "The routing preference.",
                  minOccurs = 0, maxOccurs = 1, allowedValues = {"fastest", "shortest"},
                  binding = LiteralStringType.class)
    default void setPreference(String preference) {
        setPreference(Optional.ofNullable(preference).map(Preference::fromValue).orElse(Preference.FASTEST));
    }

    void setPreference(Preference preference);

    @Execute
    void execute() throws Exception;

    @ComplexOutput(identifier = Outputs.ROUTE, title = "The Route.", abstrakt = "The computed route.",
                   binding = RouteData.class)
    Route getRoute();

}
