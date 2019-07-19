package org.n52.testbed.routing;

import org.locationtech.jts.geom.MultiPoint;
import org.n52.javaps.algorithm.annotation.ComplexInput;
import org.n52.testbed.routing.io.geojson.MultiPointData;
import org.n52.testbed.routing.model.wps.Inputs;

public interface SupportsIntermediates extends RoutingAlgorithm {

    @ComplexInput(identifier = Inputs.INTERMEDIATES, title = "Intermediate Points", abstrakt = "Intermediate points along the route.", minOccurs = 0, maxOccurs = 1, binding = MultiPointData.class)
    void setIntermediates(MultiPoint intermediates);
}
