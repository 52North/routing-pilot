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
