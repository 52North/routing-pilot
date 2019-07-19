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
package org.n52.testbed.routing.here;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.n52.javaps.algorithm.ExecutionException;
import org.n52.javaps.algorithm.annotation.Algorithm;
import org.n52.testbed.routing.AbstractRoutingAlgorithm;
import org.n52.testbed.routing.SupportsIntermediates;
import org.n52.testbed.routing.SupportsMaxHeight;
import org.n52.testbed.routing.SupportsMaxWeight;
import org.n52.testbed.routing.SupportsObstacles;
import org.n52.testbed.routing.SupportsWhen;
import org.n52.testbed.routing.model.routing.Instruction;
import org.n52.testbed.routing.model.routing.LevelOfDetail;
import org.n52.testbed.routing.model.routing.Preference;
import org.n52.testbed.routing.model.routing.Route;
import org.n52.testbed.routing.model.routing.RouteFeature;
import org.n52.testbed.routing.model.routing.RouteFeatureProperties;
import org.n52.testbed.routing.model.routing.RouteFeatureType;
import org.n52.testbed.routing.model.routing.RouteOverviewProperties;
import org.n52.testbed.routing.model.routing.RouteSegmentProperties;
import org.n52.testbed.routing.model.routing.RouteStartOrEndProperties;
import org.n52.testbed.routing.model.routing.When;
import org.n52.testbed.routing.model.wps.Status;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Algorithm(identifier = "org.n52.routing.here", version = "1.0.0")
public class HereAlgorithm extends AbstractRoutingAlgorithm
        implements SupportsIntermediates, SupportsObstacles, SupportsMaxHeight, SupportsMaxWeight, SupportsWhen {
    public static final String HERE_APP_ID_PROPERTY = "here.app.id";
    public static final String HERE_APP_CODE_PROPERTY = "here.app.code";
    public static final String HERE_APP_CODE_ENV_VARIABLE = "HERE_APP_CODE";
    public static final String HERE_APP_ID_ENV_VARIABLE = "HERE_APP_ID";
    private static final HttpUrl BASE_URL = HttpUrl.get("https://route.api.here.com/routing/7.2/");
    private static final String APP_CODE = "app_code";
    private static final String APP_ID = "app_id";
    private MultiPoint intermediates;
    private MultiPolygon obstacles;
    private BigDecimal maxHeight;
    private BigDecimal maxWeight;
    private When when;
    private OkHttpClient client;
    @Autowired
    private ObjectMapper objectMapper;

    public HereAlgorithm() {
        this.client = createOkHttpClient();
    }

    @Override
    public void setIntermediates(MultiPoint intermediates) {
        this.intermediates = intermediates;
    }

    @Override
    public void setObstacles(MultiPolygon obstacles) {
        this.obstacles = obstacles;
    }

    @Override
    public void setMaxHeight(BigDecimal maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public void setMaxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
    }

    @Override
    public void setWhen(When when) {
        this.when = when;
    }

    private OkHttpClient createOkHttpClient() {
        String appCode = requireProperty(HERE_APP_CODE_ENV_VARIABLE, HERE_APP_CODE_PROPERTY);
        String appId = requireProperty(HERE_APP_ID_ENV_VARIABLE, HERE_APP_ID_PROPERTY);
        return new OkHttpClient.Builder()
                       .addInterceptor(new AppCredentialInterceptor(appId, appCode))
                       .retryOnConnectionFailure(true)
                       .followRedirects(true)
                       .build();
    }

    private String requireProperty(String envName, String propName) {
        String value = System.getenv(envName);
        if (value == null || value.isEmpty()) {
            value = System.getProperty(propName);
        }
        if (value == null || value.isEmpty()) {
            String message = String.format("neither system property %s nor environment variable %s is set",
                                           propName, envName);
            throw new RuntimeException(message);
        }
        return value;
    }

    @Override
    public Route computeRoute() throws IOException, ExecutionException {
        HttpUrl url = buildRequestUrl();

        HereApi.Route hereRoute = sendRequest(url);

        return createRoute(hereRoute);

    }

    private HttpUrl buildRequestUrl() throws ExecutionException {
        HttpUrl.Builder urlBuilder = BASE_URL.newBuilder().addPathSegment("calculateroute.json");

        HereApi.TransportMode transportMode = (this.maxWeight != null || this.maxHeight != null)
                                              ? HereApi.TransportMode.truck : HereApi.TransportMode.car;
        HereApi.RoutingType routingType = getPreference() == Preference.SHORTEST
                                          ? HereApi.RoutingType.shortest : HereApi.RoutingType.fastest;

        if (this.when != null) {
            switch (this.when.getType()) {
                case DEPARTURE:
                    urlBuilder.addQueryParameter("departure", this.when.getTimestamp().toString());
                    break;
                case ARRIVAL:
                    urlBuilder.addQueryParameter("arrival", this.when.getTimestamp().toString());
                    break;
                default:
                    throw new ExecutionException("unsupported when type");
            }
        }

        if (this.maxHeight != null) {
            urlBuilder.addQueryParameter("height", this.maxHeight.toPlainString());
        }
        if (this.maxWeight != null) {
            urlBuilder.addQueryParameter("limitedWeight", this.maxWeight.toPlainString());
        }
        if (this.obstacles != null && !this.obstacles.isEmpty()) {
            urlBuilder.addQueryParameter("avoidAreas", getAvoidAreasParameter(this.obstacles));
        }

        int idx = 0;
        String waypointFormat = "waypoint%d";
        urlBuilder.addQueryParameter(String.format(waypointFormat, idx++), asWaypointParameter(getStartPoint()));
        if (this.intermediates != null) {
            int n = this.intermediates.getNumGeometries();
            for (int i = 0; i < n; i++) {
                urlBuilder.addQueryParameter(String.format(waypointFormat, idx++),
                                             asWaypointParameter((Point) this.intermediates.getGeometryN(i)));
            }
        }
        urlBuilder.addQueryParameter(String.format(waypointFormat, idx), asWaypointParameter(getEndPoint()));

        urlBuilder.addQueryParameter("routeAttributes", getRouteAttributesParameter());
        urlBuilder.addQueryParameter("instructionFormat", getInstructionFormatParameter());
        urlBuilder.addQueryParameter("legAttributes", getLegAttributes());
        urlBuilder.addQueryParameter("maneuverAttributes", getManeuverAttributesParameter());
        urlBuilder.addQueryParameter("metricSystem", getMetricSystemParameter());
        urlBuilder.addQueryParameter("mode", getModeParameter(transportMode, routingType));

        return urlBuilder.build();
    }

    private HereApi.Route sendRequest(HttpUrl url) throws IOException, ExecutionException {
        Request.Builder requestBuilder = new Request.Builder();

        Request request = requestBuilder.get().url(url).build();
        Response execute = client.newCall(request).execute();
        if (!execute.isSuccessful() || execute.body() == null) {
            throw new ExecutionException(String.format("HERE API responded with status %d", execute.code()));
        }

        return objectMapper.readValue(execute.body().charStream(), HereApi.ResponseWrapper.class)
                           .getResponse().getRoute().iterator().next();
    }

    private String getModeParameter(HereApi.TransportMode transportMode, HereApi.RoutingType routingType) {
        return String.format("%s;%s;traffic:default", routingType, transportMode);
    }

    private String getMetricSystemParameter() {
        return HereApi.UnitSystem.metric.toString();
    }

    private String getRouteAttributesParameter() {
        return Stream.of(HereApi.RouteAttributeType.summary,
                         HereApi.RouteAttributeType.shape, HereApi.RouteAttributeType.legs)
                     .map(HereApi.RouteAttributeType::toString).collect(joining(","));
    }

    private String getInstructionFormatParameter() {
        return HereApi.InstructionFormat.text.toString();
    }

    private String getLegAttributes() {
        return Stream.of(
                HereApi.RouteLegAttributeType.waypoint,
                HereApi.RouteLegAttributeType.maneuvers,
                HereApi.RouteLegAttributeType.length,
                HereApi.RouteLegAttributeType.travelTime,
                HereApi.RouteLegAttributeType.shape)
                     .map(HereApi.RouteLegAttributeType::toString).collect(joining(","));
    }

    private String getManeuverAttributesParameter() {
        return Stream.of(
                HereApi.ManeuverAttributeType.length,
                HereApi.ManeuverAttributeType.shape,
                HereApi.ManeuverAttributeType.time,
                HereApi.ManeuverAttributeType.trafficTime,
                HereApi.ManeuverAttributeType.direction,
                HereApi.ManeuverAttributeType.roadName)
                     .map(HereApi.ManeuverAttributeType::toString).collect(joining(","));
    }

    private Route createRoute(HereApi.Route hereRoute) {
        Route route = new Route();
        route.setName(getName());
        route.setStatus(Status.SUCCESSFUL);
        List<RouteFeature> features = new ArrayList<>();
        features.add(createOverview(hereRoute));
        features.add(createStart());
        features.addAll(createSegments(hereRoute));
        features.add(createEnd());
        route.setFeatures(features);
        return route;
    }

    private RouteFeature createEnd() {
        RouteStartOrEndProperties routeStartOrEndProperties = new RouteStartOrEndProperties(RouteFeatureType.END);
        return new RouteFeature(getEndPoint(), createFeatureProperties(routeStartOrEndProperties));
    }

    private RouteFeature createStart() {
        RouteStartOrEndProperties properties = new RouteStartOrEndProperties(RouteFeatureType.START);
        return new RouteFeature(getStartPoint(), createFeatureProperties(properties));
    }

    private Map<String, Object> createFeatureProperties(RouteFeatureProperties<?> properties) {
        MapLikeType type = TypeFactory.defaultInstance()
                                      .constructMapLikeType(Map.class, String.class, Object.class);
        return objectMapper.convertValue(properties, type);
    }

    private RouteFeature createOverview(HereApi.Route hereRoute) {
        RouteOverviewProperties props = new RouteOverviewProperties();
        props.setMaxHeight(null);
        props.setMaxLoad(null);
        if (hereRoute.getSummary() != null) {
            props.setDuration(hereRoute.getSummary().getDuration());
            props.setLength(hereRoute.getSummary().getLength());
        }
        return new RouteFeature(hereRoute.getShape(), createFeatureProperties(props));
    }

    private List<RouteFeature> createSegments(HereApi.Route hereRoute) {
        return hereRoute.getLegs().stream()
                        .map(HereApi.Leg::getManeuvers)
                        .flatMap(List::stream)
                        .map(this::getRouteSegmentFromManeuver)
                        .filter(Objects::nonNull)
                        .collect(toList());
    }

    private RouteFeature getRouteSegmentFromManeuver(HereApi.Maneuver m) {
        if (!(m.getShape() instanceof LineString)) {
            return null;
        }
        RouteSegmentProperties props = new RouteSegmentProperties();
        props.setDuration(m.getDuration());
        props.setInstructions(getInstructionFromDirection(m.getDirection()));
        props.setLength(m.getLength());
        props.setLevelOfDetail(LevelOfDetail.VISUALIZATION);
        props.setMaxHeight(m.getMaxHeight());
        props.setMaxLoad(m.getMaxLoad());
        props.setSpeedLimit(m.getSpeedLimit());
        props.setSpeedLimitUnit(m.getSpeedLimitUnit());
        props.setRoadName(m.getRoadName());
        props.setAdditionalProperty("text", m.getInstruction());

        return new RouteFeature(m.getShape(), createFeatureProperties(props));
    }

    private Instruction getInstructionFromDirection(String direction) {
        if ("left".equals(direction)) {
            return Instruction.LEFT;
        } else if ("right".equals(direction)) {
            return Instruction.RIGHT;
        } else if ("forward".equals(direction)) {
            return Instruction.CONTINUE;
        } else {
            return null;
        }
    }

    private String getAvoidAreasParameter(MultiPolygon obstacles) {
        return IntStream.range(0, obstacles.getNumGeometries())
                        .mapToObj(obstacles::getGeometryN)
                        .map(Geometry::getEnvelopeInternal)
                        .map(envelope -> {
                            // topLeft;bottomRight
                            return String.format(Locale.ROOT, "%f,%f;%f,%f", envelope.getMaxY(), envelope.getMinX(),
                                                 envelope.getMinY(), envelope.getMaxX());
                        }).collect(joining("!"));
    }

    private String asWaypointParameter(Point startPoint) {
        return String.format(Locale.ROOT, "geo!%f,%f", startPoint.getY(), startPoint.getX());
    }

    private static final class AppCredentialInterceptor implements Interceptor {
        private final String appId;
        private final String appCode;

        AppCredentialInterceptor(String appId, String appCode) {
            this.appId = appId;
            this.appCode = appCode;
        }

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl url = request.url().newBuilder()
                                 .addQueryParameter(APP_ID, appId)
                                 .addQueryParameter(APP_CODE, appCode)
                                 .build();
            return chain.proceed(request.newBuilder().url(url).build());
        }
    }

}
