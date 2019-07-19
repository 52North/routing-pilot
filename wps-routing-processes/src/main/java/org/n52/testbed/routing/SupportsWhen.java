package org.n52.testbed.routing;

import org.n52.javaps.algorithm.annotation.ComplexInput;
import org.n52.testbed.routing.io.WhenData;
import org.n52.testbed.routing.model.routing.When;
import org.n52.testbed.routing.model.wps.Inputs;

public interface SupportsWhen extends RoutingAlgorithm {

    @ComplexInput(identifier = Inputs.WHEN, title = "When", abstrakt = "When to arrive or when to departure.", minOccurs = 0, maxOccurs = 1, binding = WhenData.class)
    void setWhen(When when);

}
