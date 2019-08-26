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
package org.n52.testbed.routing.ecere;

import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.n52.javaps.algorithm.annotation.Algorithm;
import org.n52.testbed.routing.DelegatingRoutingAlgorithm;
import org.n52.testbed.routing.SupportsIntermediates;
import org.n52.testbed.routing.SupportsMaxHeight;
import org.n52.testbed.routing.SupportsMaxWeight;
import org.n52.testbed.routing.model.routing.RouteDefinition;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Algorithm(identifier = "org.n52.routing.ecere", version = "1.0.0")
public class EcereAlgorithm extends DelegatingRoutingAlgorithm
        implements SupportsIntermediates, SupportsMaxWeight, SupportsMaxHeight {
    private MultiPoint intermediates;
    private BigDecimal maxHeight;
    private BigDecimal maxWeight;

    @Override
    protected RouteDefinition createRouteDefinition() {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setName(getName());
        routeDefinition.setPreference(getPreference());
        routeDefinition.setWaypoints(getWayPoints());
        routeDefinition.setMaxHeight(getMaxHeight());
        routeDefinition.setMaxWeight(getMaxWeight());
        return routeDefinition;
    }

    @Override
    protected MultiPoint getWayPoints() {
        Point[] points = Stream.of(Stream.of(getStartPoint()),
                                   Optional.ofNullable(intermediates)
                                           .map(mp -> IntStream.range(0, mp.getNumGeometries())
                                                               .mapToObj(mp::getGeometryN)
                                                               .map(Point.class::cast))
                                           .orElseGet(Stream::empty),
                                   Stream.of(getEndPoint()))
                               .flatMap(Function.identity())
                               .toArray(Point[]::new);

        return getGeometryFactory().createMultiPoint(points);
    }

    @Override
    protected String getDelegatingEndpoint() {
        return "http://maps.ecere.com/route";
    }

    @Override
    public void setIntermediates(MultiPoint intermediates) {
        this.intermediates = intermediates;
    }

    public MultiPoint getIntermediates() {
        return intermediates;
    }

    @Override
    public void setMaxHeight(BigDecimal maxHeight) {
        this.maxHeight = maxHeight;
    }

    public BigDecimal getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setMaxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
    }

    public BigDecimal getMaxWeight() {
        return maxWeight;
    }
}
