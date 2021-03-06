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

import org.locationtech.jts.geom.Geometry;
import org.n52.testbed.routing.model.Feature;
import org.n52.testbed.routing.model.Link;

import java.util.List;
import java.util.Map;

public class RouteFeature extends Feature<RouteFeature> {
    private static final long serialVersionUID = 7485879199206601838L;

    public RouteFeature() {
    }

    public RouteFeature(Geometry geometry) {
        super(geometry);
    }

    public RouteFeature(Geometry geometry, Map<String, Object> properties) {
        super(geometry, properties);
    }

    public RouteFeature(Object id, Geometry geometry, Map<String, Object> properties) {
        super(id, geometry, properties);
    }

    public RouteFeature(Object id, Geometry geometry, Map<String, Object> properties, List<Link> links) {
        super(id, geometry, properties, links);
    }
}
