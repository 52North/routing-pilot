package org.n52.testbed.routing.skymantics;

import org.n52.javaps.algorithm.annotation.Algorithm;
import org.n52.javaps.algorithm.annotation.LiteralInput;
import org.n52.javaps.io.literal.xsd.LiteralStringType;
import org.n52.testbed.routing.DelegatingRoutingAlgorithm;
import org.n52.testbed.routing.SupportsAlgorithm;
import org.n52.testbed.routing.SupportsDataset;
import org.n52.testbed.routing.model.routing.RouteDefinition;
import org.n52.testbed.routing.model.wps.Inputs;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Algorithm(identifier = "org.n52.routing.skymantics", version = "1.0.0")
public class SkymanticsRoutingAlgorithm extends DelegatingRoutingAlgorithm {
        // implements SupportsAlgorithm, SupportsDataset {

    private static final String ALGORITHM_ASTAR_CH = "astar-ch";
    private static final String DATASET_HERE = "HERE";
    private static final String DATASET_OSM = "OSM";
    private static final String DATASET_NSG = "NSG";
    private static final String ALGORITHM_DIJKSTRA_CH = "dijkstra-ch";
    private static final String ALGORITHM_DIJKSTRA_BI = "dijkstra-bi";
    private static final String ALGORITHM_DIJKSTRA = "dijkstra";
    private static final String ALGORITHM_ASTAR = "astar";
    private static final String ALGORITHM_ASTAR_BI = "astar-bi";

    private static final Set<String> ALGORITHMS_SET = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(ALGORITHM_DIJKSTRA,
                                        ALGORITHM_DIJKSTRA_BI,
                                        ALGORITHM_DIJKSTRA_CH,
                                        ALGORITHM_ASTAR,
                                        ALGORITHM_ASTAR_BI,
                                        ALGORITHM_ASTAR_CH)));
    private static final Set<String> DATASETS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(DATASET_OSM,
                                        DATASET_HERE,
                                        DATASET_NSG)));

    private static final String DEFAULT_ALGORITHM = ALGORITHM_ASTAR_CH;
    private static final String DEFAULT_DATASET = DATASET_HERE;

    private String algorithm;
    private String dataset;

    @LiteralInput(identifier = Inputs.ALGORITHM, title = "Algorithm", defaultValue = DEFAULT_ALGORITHM,
                  allowedValues = {ALGORITHM_DIJKSTRA, ALGORITHM_DIJKSTRA_BI, ALGORITHM_DIJKSTRA_CH,
                                   ALGORITHM_ASTAR, ALGORITHM_ASTAR_BI, ALGORITHM_ASTAR_CH},
                  abstrakt = "The algorithm to use for route computation.", minOccurs = 0, maxOccurs = 1,
                  binding = LiteralStringType.class)
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    private String getAlgorithm() {
        return Optional.ofNullable(this.algorithm).map(String::toLowerCase)
                       .filter(ALGORITHMS_SET::contains).orElse(DEFAULT_ALGORITHM);
    }

    @LiteralInput(identifier = Inputs.DATASET, title = "Dataset", defaultValue = DEFAULT_DATASET,
                  allowedValues = {DATASET_OSM, DATASET_HERE, DATASET_NSG},
                  abstrakt = "The dataset to use for route computation.", minOccurs = 0, maxOccurs = 1,
                  binding = LiteralStringType.class)
    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    private String getDataset() {
        return Optional.ofNullable(this.dataset).map(String::toUpperCase)
                       .filter(DATASETS::contains).orElse(DEFAULT_DATASET);
    }

    @Override
    protected RouteDefinition createRouteDefinition() {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setName(getName());
        routeDefinition.setPreference(getPreference());
        routeDefinition.setWaypoints(getWayPoints());
        routeDefinition.setAlgorithm(getAlgorithm());
        routeDefinition.setDataset(getDataset());
        return routeDefinition;
    }

    @Override
    protected String getDelegatingEndpoint() {
        return "https://skymanticsroutingengine.azurewebsites.net/routing";
    }

}
