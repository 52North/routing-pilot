package org.n52.testbed.routing;

import org.n52.javaps.algorithm.annotation.LiteralInput;
import org.n52.javaps.io.literal.xsd.LiteralDecimalType;
import org.n52.testbed.routing.model.Units;
import org.n52.testbed.routing.model.wps.Inputs;

import java.math.BigDecimal;

public interface SupportsMaxWeight extends RoutingAlgorithm {

    @LiteralInput(identifier = Inputs.MAX_WEIGHT, title = "Maximum Weight", abstrakt = "The maximum weight the vehicle has.", minOccurs = 0, maxOccurs = 1, uom = Units.TON, binding = LiteralDecimalType.class)
    void setMaxWeight(BigDecimal maxWeight);
}
