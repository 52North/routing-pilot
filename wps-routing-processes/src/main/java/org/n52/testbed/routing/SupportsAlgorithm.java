package org.n52.testbed.routing;

import org.n52.javaps.algorithm.annotation.LiteralInput;
import org.n52.javaps.io.literal.xsd.LiteralStringType;
import org.n52.testbed.routing.model.wps.Inputs;

public interface SupportsAlgorithm extends RoutingAlgorithm {

    @LiteralInput(identifier = Inputs.ALGORITHM, title = "Algorithm", abstrakt = "The algorithm to use for route computation.", minOccurs = 0, maxOccurs = 1, binding = LiteralStringType.class)
    void setAlgorithm(String algorithm);
}
