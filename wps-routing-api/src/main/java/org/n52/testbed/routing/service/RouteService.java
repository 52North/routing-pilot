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
package org.n52.testbed.routing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bson.types.ObjectId;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.n52.testbed.routing.api.impl.HttpStatusError;
import org.n52.testbed.routing.client.OgcProcessingApi;
import org.n52.testbed.routing.model.MediaTypes;
import org.n52.testbed.routing.model.Units;
import org.n52.testbed.routing.model.routing.Preference;
import org.n52.testbed.routing.model.routing.Route;
import org.n52.testbed.routing.model.routing.RouteDefinition;
import org.n52.testbed.routing.model.routing.RouteFeature;
import org.n52.testbed.routing.model.routing.RouteFeatureProperties;
import org.n52.testbed.routing.model.routing.RouteInfo;
import org.n52.testbed.routing.model.routing.RouteOverviewProperties;
import org.n52.testbed.routing.model.routing.RouteStartOrEndProperties;
import org.n52.testbed.routing.model.wps.ComplexData;
import org.n52.testbed.routing.model.wps.Execute;
import org.n52.testbed.routing.model.wps.Format;
import org.n52.testbed.routing.model.wps.Formats;
import org.n52.testbed.routing.model.wps.Input;
import org.n52.testbed.routing.model.wps.InputDescription;
import org.n52.testbed.routing.model.wps.Inputs;
import org.n52.testbed.routing.model.wps.LiteralData;
import org.n52.testbed.routing.model.wps.Output;
import org.n52.testbed.routing.model.wps.Outputs;
import org.n52.testbed.routing.model.wps.ProcessOffering;
import org.n52.testbed.routing.model.wps.Result;
import org.n52.testbed.routing.model.wps.ResultOutputs;
import org.n52.testbed.routing.model.wps.ResultValue;
import org.n52.testbed.routing.model.wps.Status;
import org.n52.testbed.routing.model.wps.StatusInfo;
import org.n52.testbed.routing.model.wps.TransmissionMode;
import org.n52.testbed.routing.model.wps.ValueType;
import org.n52.testbed.routing.persistence.MongoRoute;
import org.n52.testbed.routing.persistence.RouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class RouteService {
    private static final Logger LOG = LoggerFactory.getLogger(RouteService.class);
    private RouteRepository repository;
    private OgcProcessingApi wpsClient;
    private String processId;
    private Duration pollDelay;
    private ObjectMapper objectMapper;
    private NotifyService notifyService;

    @Autowired
    public void setNotifyService(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    @Autowired
    public void setRouteRepository(RouteRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setWpsClient(OgcProcessingApi wpsClient) {
        this.wpsClient = wpsClient;
    }

    @Value("${routing.process}")
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Value("${routing.polling.delay}")
    public void setPollDelay(Duration pollDelay) {
        this.pollDelay = pollDelay;
    }

    @Scheduled(fixedDelayString = "${routing.polling.delay}")
    public void notifySubscribers() {
        repository.findBySubscriberNotNull().stream().parallel().forEach(this::requestStatus);
    }

    @SuppressFBWarnings("SF_SWITCH_FALLTHROUGH")
    private void requestStatus(MongoRoute mongoRoute) {
        Route route = getRoute(mongoRoute.getIdentifier());

        switch (route.getStatus()) {
            case FAILED:
            case SUCCESSFUL:
                mongoRoute.setSubscriber(null);
                this.repository.save(mongoRoute);
                this.notifyService.notifySubscriber(mongoRoute, mongoRoute.getSubscriber(), route);
                break;
            default:
                LOG.info("Route {} is still in {} state", mongoRoute.getIdentifier(), route.getStatus());
                break;
        }
    }

    public RouteInfo createRouteAsync(@Valid RouteDefinition routeDefinition) {
        try {
            String jobId = execute(routeDefinition);
            MongoRoute mongoRoute = new MongoRoute(processId, jobId);
            mongoRoute.setDefinition(routeDefinition);
            mongoRoute.setSubscriber(routeDefinition.getSubscriber());
            return repository.save(mongoRoute);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RouteInfo createRouteAsync(@Valid Execute execute) {
        try {
            RouteDefinition routeDefinition = createRouteDefinitionFromExecute(execute);
            convertToInternalExecute(execute);
            String jobId = execute(execute);
            MongoRoute mongoRoute = new MongoRoute(processId, jobId);
            mongoRoute.setDefinition(routeDefinition);
            return repository.save(mongoRoute);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void convertToInternalExecute(Execute execute) {
        Map<String, Input> inputs = execute.getInputMap();
        if (inputs.containsKey(Inputs.WAYPOINTS)) {
            Object input = inputs.get(Inputs.WAYPOINTS).getInput();
            MultiPoint waypoints = createMultiPoint(createComplexData(input).getValue().getInlineValue());
            Format geoJsonFormat = new Format(MediaTypes.APPLICATION_GEO_JSON);

            MultiPoint intermediates = getIntermediatePoints(waypoints);
            if (!intermediates.isEmpty()) {
                inputs.put(Inputs.INTERMEDIATES,
                           new Input(Inputs.INTERMEDIATES,
                                     new ComplexData(newInlineValue(intermediates),
                                                     geoJsonFormat)));
            }

            inputs.put(Inputs.START, new Input(Inputs.START,
                                               new ComplexData(newInlineValue(getStartPoint(waypoints)),
                                                               geoJsonFormat)));
            inputs.put(Inputs.END, new Input(Inputs.END,
                                             new ComplexData(newInlineValue(getEndPoint(waypoints)),
                                                             geoJsonFormat)));
            inputs.remove(Inputs.WAYPOINTS);
            execute.setInputs(new ArrayList<>(inputs.values()));
        }
    }

    private ValueType newInlineValue(Object value) {
        return new ValueType().inlineValue(value);
    }

    private RouteDefinition createRouteDefinitionFromExecute(Execute execute) {
        RouteDefinition routeDefinition = new RouteDefinition();
        Map<String, Input> inputs = execute.getInputMap();
        convertLiteralValue(Inputs.NAME, inputs, routeDefinition::setName);
        convertLiteralValue(Inputs.ALGORITHM, inputs, routeDefinition::setAlgorithm);
        convertLiteralValue(Inputs.DATASET, inputs, routeDefinition::setDataset);
        convertLiteralValue(Inputs.ENGINE, inputs, routeDefinition::setEngine);
        convertLiteralValue(Inputs.PREFERENCE, inputs, Preference::fromValue, routeDefinition::setPreference);
        convertLiteralValue(Inputs.MAX_HEIGHT, inputs, BigDecimal::new, routeDefinition::setMaxHeight);
        convertLiteralValue(Inputs.MAX_WEIGHT, inputs, BigDecimal::new, routeDefinition::setMaxWeight);
        convertComplexValue(Inputs.OBSTACLES, inputs, this::createMultiPolygon, routeDefinition::setObstacles);
        convertComplexValue(Inputs.WAYPOINTS, inputs, this::createMultiPoint, routeDefinition::setWaypoints);
        return routeDefinition;

    }

    private ComplexData createComplexData(Object input) {
        if (input == null) {
            return null;
        }
        return objectMapper.convertValue(input, ComplexData.class);
    }

    private LiteralData createLiteralData(Object input) {
        if (input == null) {
            return null;
        }
        return objectMapper.convertValue(input, LiteralData.class);
    }

    private MultiPoint createMultiPoint(Object x) {
        if (x == null) {
            return null;
        }
        return objectMapper.convertValue(x, MultiPoint.class);
    }

    private MultiPolygon createMultiPolygon(Object x) {
        if (x == null) {
            return null;
        }
        return objectMapper.convertValue(x, MultiPolygon.class);
    }

    private void convertLiteralValue(String input, Map<String, Input> inputs, Consumer<String> consumer) {
        convertLiteralValue(input, inputs, x -> x, consumer);
    }

    private <T> void convertLiteralValue(String input, Map<String, Input> inputs,
                                         Function<String, T> converter, Consumer<T> consumer) {
        Optional.ofNullable(inputs.get(input)).map(Input::getInput).map(this::createLiteralData)
                .map(LiteralData::getValue).map(converter).ifPresent(consumer);
    }

    private <T> void convertComplexValue(String input, Map<String, Input> inputs, Function<Object, T> converter,
                                         Consumer<T> consumer) {
        Optional.ofNullable(inputs.get(input)).map(Input::getInput).map(this::createComplexData)
                .map(ComplexData::getValue).map(ValueType::getInlineValue).map(converter).ifPresent(consumer);
    }

    private String execute(RouteDefinition routeDefinition) throws IOException {
        return execute(createExecuteFromRouteDefinition(routeDefinition));
    }

    private String execute(Execute execute) throws IOException {
        LOG.info("Sending execute request: {}", execute);
        return getJobId(HttpStatusError.throwIfNotSuccessful(wpsClient.execute(processId, execute).execute()));
    }

    private Execute createExecuteFromRouteDefinition(RouteDefinition routeDefinition) throws IOException {
        Map<String, InputDescription> inputDescriptions = getInputDescriptions(processId);

        Optional<Input> algorithm = Optional.ofNullable(routeDefinition.getAlgorithm()).filter(this::isNotEmpty)
                                            .map(LiteralData::new).map(x -> new Input(Inputs.ALGORITHM, x));
        Optional<Input> engine = Optional.ofNullable(routeDefinition.getEngine()).filter(this::isNotEmpty)
                                         .map(LiteralData::new).map(x -> new Input(Inputs.ENGINE, x));
        Optional<Input> dataset = Optional.ofNullable(routeDefinition.getDataset()).filter(this::isNotEmpty)
                                          .map(LiteralData::new).map(x -> new Input(Inputs.DATASET, x));
        Optional<Input> preference = Optional.ofNullable(routeDefinition.getPreference()).map(Preference::toString)
                                             .map(LiteralData::new).map(x -> new Input(Inputs.PREFERENCE, x));
        Optional<Input> maxHeight = Optional.ofNullable(routeDefinition.getMaxHeight()).map(BigDecimal::toPlainString)
                                            .map(x -> new LiteralData(x, Units.TON_REFERENCE))
                                            .map(x -> new Input(Inputs.MAX_HEIGHT, x));
        Optional<Input> maxWeight = Optional.ofNullable(routeDefinition.getMaxWeight()).map(BigDecimal::toPlainString)
                                            .map(x -> new LiteralData(x, Units.METER_REFERENCE))
                                            .map(x -> new Input(Inputs.MAX_WEIGHT, x));
        Optional<Input> name = Optional.ofNullable(routeDefinition.getName()).filter(this::isNotEmpty)
                                       .map(LiteralData::new).map(x -> new Input(Inputs.NAME, x));
        Optional<Input> start = Optional.ofNullable(routeDefinition.getWaypoints()).map(this::getStartPoint)
                                        .map(this::getGeoJsonData).map(x -> new Input(Inputs.START, x));
        Optional<Input> end = Optional.ofNullable(routeDefinition.getWaypoints()).map(this::getEndPoint)
                                      .map(this::getGeoJsonData).map(x -> new Input(Inputs.END, x));
        Optional<Input> obstacle = Optional.ofNullable(routeDefinition.getObstacles()).map(this::getGeoJsonData)
                                           .map(x -> new Input(Inputs.OBSTACLES, x));
        Optional<Input> when = Optional.ofNullable(routeDefinition.getWhen()).map(this::getJsonData)
                                       .map(x -> new Input(Inputs.WHEN, x));
        Optional<Input> intermediateWaypoints = Optional.ofNullable(routeDefinition.getWaypoints())
                                                        .map(this::getIntermediatePoints).filter(x -> !x.isEmpty())
                                                        .map(this::getGeoJsonData)
                                                        .map(x -> new Input(Inputs.WAYPOINTS, x));

        List<Input> inputs = Stream.of(preference, algorithm, engine, dataset, maxHeight, maxWeight, name,
                                       intermediateWaypoints, obstacle, when, start, end)
                                   .filter(Optional::isPresent).map(Optional::get)
                                   .filter(input -> inputDescriptions.containsKey(input.getId()))
                                   .collect(toList());

        Output output = new Output();
        output.setId(Outputs.ROUTE);
        output.setFormat(new Format(MediaTypes.APPLICATION_GEO_JSON));
        output.setTransmissionMode(TransmissionMode.VALUE);

        return new Execute(inputs, Collections.singletonList(output));
    }

    public Route createRouteSync(@Valid RouteDefinition routeDefinition) {
        try {
            return pollResult(routeDefinition, execute(routeDefinition));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Route pollResult(@Valid RouteDefinition routeDefinition, String jobId) {
        while (true) {
            try {
                Thread.sleep(pollDelay.toMillis());

                StatusInfo statusInfo = retrieveStatus(processId, jobId);

                switch (statusInfo.getStatus()) {
                    case SUCCESSFUL:
                        return retrieveResult(processId, jobId);
                    case FAILED:
                        return createFailureRoute(routeDefinition);
                    default:
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Geometry getStartPoint(MultiPoint x) {
        return x.getGeometryN(0);
    }

    private Geometry getEndPoint(MultiPoint x) {
        return x.getGeometryN(x.getNumGeometries() - 2);
    }

    private MultiPoint getIntermediatePoints(MultiPoint x) {
        Point[] intermediates = IntStream.range(1, x.getNumGeometries() - 1)
                                         .mapToObj(x::getGeometryN).map(Point.class::cast).toArray(Point[]::new);
        return x.getFactory().createMultiPoint(intermediates);
    }

    @NotNull
    private Map<String, InputDescription> getInputDescriptions(String routingProcessId) throws IOException {
        return getProcessOffering(routingProcessId).getProcess().getInputs().stream()
                                                   .collect(toMap(InputDescription::getId, Function.identity()));
    }

    @NotNull
    private ProcessOffering getProcessOffering(String routingProcessId) throws IOException {
        LOG.info("Sending get process offering request for process: {}", routingProcessId);
        Response<ProcessOffering> processOfferingResponse = wpsClient.getProcessDescription(routingProcessId).execute();

        return HttpStatusError.throwIfNotSuccessful(processOfferingResponse).body();
    }

    private String getJobId(Response<Void> executeResponse) {
        String location = executeResponse.headers().get(HttpHeaders.LOCATION);
        String[] split = location.split("/");
        return split[split.length - 1];
    }

    @NotNull
    private ComplexData getGeoJsonData(@NotNull Geometry x) {
        return new ComplexData(newInlineValue(x), Formats.GEO_JSON_FORMAT);
    }

    @NotNull
    private ComplexData getJsonData(@NotNull Object x) {
        return new ComplexData(newInlineValue(x), Formats.JSON_FORMAT);
    }

    private boolean isNotEmpty(@NotNull String x) {
        return !x.isEmpty();
    }

    public Stream<RouteInfo> getRoutes() {
        return repository.findAll().stream().map(RouteInfo.class::cast);
    }

    public void deleteRoute(@NotNull String routeId) throws RouteNotFoundException {
        ObjectId id = new ObjectId(routeId);
        if (!repository.existsById(id)) {
            throw new RouteNotFoundException();
        }
        repository.deleteById(id);
    }

    public RouteDefinition getRouteDefinition(@NotNull String routeId) throws RouteNotFoundException {
        return repository.findById(new ObjectId(routeId)).orElseThrow(RouteNotFoundException::new).getDefinition();
    }

    public Route getRoute(@NotNull String routeId) throws RouteNotFoundException {
        MongoRoute mongoRoute = repository.findById(new ObjectId(routeId)).orElseThrow(RouteNotFoundException::new);
        if (mongoRoute.getRoute() != null) {
            return mongoRoute.getRoute();
        }
        try {
            // check the status of the job
            StatusInfo statusInfo = retrieveStatus(mongoRoute);
            if (!statusInfo.getStatus().isFinished()) {
                // create an "empty" route and set the status
                return createEmptyRoute(mongoRoute.getDefinition(), statusInfo.getStatus());
            } else if (statusInfo.getStatus().isFailure()) {
                // create a failure route
                Route route = createFailureRoute(mongoRoute.getDefinition());
                // save the result
                mongoRoute.setRoute(route);
                repository.save(mongoRoute);
                return route;
            } else {
                // if the job is finished, retrieve the result
                Route route = retrieveResult(mongoRoute);
                // save the result
                mongoRoute.setRoute(route);
                repository.save(mongoRoute);
                return route;
            }
        } catch (IOException ex) {
            throw new HttpStatusError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    @NotNull
    private Route createFailureRoute(@NotNull RouteDefinition routeDefinition) {
        return createEmptyRoute(routeDefinition, Status.FAILED);
    }

    @NotNull
    private Route createEmptyRoute(@NotNull RouteDefinition routeDefinition, Status status) {
        Route route = new Route();
        route.setStatus(status);
        route.setName(routeDefinition.getName());
        List<RouteFeature> features = new ArrayList<>(3);

        Geometry waypoints = routeDefinition.getWaypoints();
        Geometry start = waypoints.getGeometryN(0);
        Geometry end = waypoints.getGeometryN(waypoints.getNumGeometries() - 1);
        // the overview
        features.add(new RouteFeature().geometry(null)
                                       .properties(createFeatureProperties(new RouteOverviewProperties())));
        features.add(new RouteFeature().geometry(start)
                                       .properties(createFeatureProperties(new RouteStartOrEndProperties())));
        features.add(new RouteFeature().geometry(end)
                                       .properties(createFeatureProperties(new RouteStartOrEndProperties())));
        route.setFeatures(features);
        return route;
    }

    @NotNull
    public StatusInfo retrieveStatus(@NotNull MongoRoute mongoRoute) throws IOException {
        return retrieveStatus(mongoRoute.getJobId(), mongoRoute.getProcessId());
    }

    @NotNull
    private StatusInfo retrieveStatus(@NotEmpty String processId, @NotEmpty String jobId) throws IOException {
        Call<StatusInfo> call = wpsClient.getStatus(processId, jobId);
        LOG.info("Retrieving status info for processId {} and jobId {}", processId, jobId);
        Response<StatusInfo> response = call.execute();
        return HttpStatusError.throwIfNotSuccessful(response).body();
    }

    @NotNull
    private Route retrieveResult(@NotNull MongoRoute mongoRoute) throws IOException {
        return retrieveResult(mongoRoute.getJobId(), mongoRoute.getProcessId());
    }

    @NotNull
    private Route retrieveResult(@NotEmpty String processId, @NotEmpty String jobId) throws IOException {
        LOG.info("Getting result for processId {} and jobId {}", processId, jobId);
        Response<Result> response = wpsClient.getResult(processId, jobId).execute();

        Result result = HttpStatusError.throwIfNotSuccessful(response).body();

        return result.getOutputs().stream()
                     .filter(output -> output.getId().equals(Outputs.ROUTE))
                     .findFirst()
                     .map(ResultOutputs::getValue)
                     .map(ResultValue::getInlineValue)
                     .orElseThrow(() -> new HttpStatusError(HttpStatus.INTERNAL_SERVER_ERROR, "no result found"));
    }

    private Map<String, Object> createFeatureProperties(RouteFeatureProperties<?> properties) {
        MapLikeType type = TypeFactory.defaultInstance()
                                      .constructMapLikeType(Map.class, String.class, Object.class);
        return objectMapper.convertValue(properties, type);
    }

}
