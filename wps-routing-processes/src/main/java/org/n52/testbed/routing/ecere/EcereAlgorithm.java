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
package org.n52.testbed.routing.ecere;

import org.locationtech.jts.geom.GeometryFactory;
import org.n52.javaps.algorithm.annotation.Algorithm;
import org.n52.testbed.routing.DelegatingRoutingAlgorithm;
import org.n52.testbed.routing.model.routing.RouteDefinition;
import org.springframework.beans.factory.annotation.Autowired;

@Algorithm(identifier = "org.n52.routing.ecere", version = "1.0.0")
public class EcereAlgorithm extends DelegatingRoutingAlgorithm {

    @Override
    protected RouteDefinition createRouteDefinition() {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setName(getName());
        routeDefinition.setPreference(getPreference());
        routeDefinition.setWaypoints(getWayPoints());
        return routeDefinition;
    }

    @Override
    protected String getDelegatingEndpoint() {
        return "http://maps.ecere.com/route";
    }

}
