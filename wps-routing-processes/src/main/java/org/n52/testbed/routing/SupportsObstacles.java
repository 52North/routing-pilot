package org.n52.testbed.routing;

import org.locationtech.jts.geom.MultiPolygon;
import org.n52.javaps.algorithm.annotation.ComplexInput;
import org.n52.testbed.routing.io.geojson.MultiPolygonData;
import org.n52.testbed.routing.model.wps.Inputs;

public interface SupportsObstacles extends RoutingAlgorithm {

    @ComplexInput(identifier = Inputs.OBSTACLES, title = "Obstacles", abstrakt = "Obstacles along the route.", minOccurs = 0, maxOccurs = 1, binding = MultiPolygonData.class)
    void setObstacles(MultiPolygon obstacles);
}
