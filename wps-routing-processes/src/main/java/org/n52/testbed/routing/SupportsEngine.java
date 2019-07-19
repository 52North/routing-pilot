package org.n52.testbed.routing;

import org.n52.javaps.algorithm.annotation.LiteralInput;
import org.n52.javaps.io.literal.xsd.LiteralStringType;
import org.n52.testbed.routing.model.wps.Inputs;

public interface SupportsEngine extends RoutingAlgorithm {

    @LiteralInput(identifier = Inputs.ENGINE, title = "Engine", abstrakt = "The engine to use for route computation.", minOccurs = 0, maxOccurs = 1, binding = LiteralStringType.class)
    void setEngine(String engine);
}
