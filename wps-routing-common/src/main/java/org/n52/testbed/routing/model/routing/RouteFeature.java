package org.n52.testbed.routing.model.routing;

import org.locationtech.jts.geom.Geometry;
import org.n52.testbed.routing.model.Feature;
import org.n52.testbed.routing.model.Link;

import java.util.List;
import java.util.Map;

public class RouteFeature extends Feature<RouteFeature> {
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
