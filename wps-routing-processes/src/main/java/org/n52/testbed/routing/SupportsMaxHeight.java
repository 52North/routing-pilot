package org.n52.testbed.routing;

import org.n52.javaps.algorithm.annotation.LiteralInput;
import org.n52.javaps.io.literal.xsd.LiteralDecimalType;
import org.n52.javaps.io.literal.xsd.LiteralStringType;
import org.n52.testbed.routing.model.Units;
import org.n52.testbed.routing.model.wps.Inputs;

import java.math.BigDecimal;

public interface SupportsMaxHeight extends RoutingAlgorithm {

    @LiteralInput(identifier = Inputs.MAX_HEIGHT, title = "Maximum Height", abstrakt = "The maximum height the vehicle has.", minOccurs = 0, maxOccurs = 1, uom = Units.METER, binding = LiteralDecimalType.class)
    void setMaxHeight(BigDecimal maxHeight);
}
