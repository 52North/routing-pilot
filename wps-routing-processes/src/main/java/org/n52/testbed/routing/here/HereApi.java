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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.locationtech.jts.geom.*;
import org.n52.testbed.routing.model.routing.SpeedLimitUnit;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public interface HereApi {
    enum UnitSystem {
        metric, imperial
    }

    enum InstructionFormat {
        text, html
    }

    enum RouteReprenstationMode {
        overview, display, dragNDrop, navigation, linkPaging, turnByTurn
    }

    enum RouteAttributeType {
        waypoints, summary, summaryByCountry, shape, boundingBox,
        legs, notes, lines, routeId, groups, tickets, incidents, zones
    }

    enum RouteLegAttributeType {
        waypoint, maneuvers, links, length, travelTime, shape, indices, boundingBox, baseTime, trafficTime, summary
    }

    enum ManeuverAttributeType {
        position, shape, travelTime, length, time, link, publicTransportLine, platform, roadName, nextRoadName,
        roadNumber, nextRoadNumber, signPost, notes, action, direction, freewayExit, freewayJunction, indices, baseTime,
        trafficTime, waitTime, boundingBox, roadShield, shapeQuality, nextManeuver, publicTransportTickets, startAngle
    }

    enum RouteLinkAttributeType {
        shape, length, speedLimit, dynamicSpeedInfo, truckRestrictions, flags, roadNumber, roadName, timezone, nextLink,
        publicTransportLine, remainTime, remainDistance, maneuver, functionalClass, nextStopName, indices, consumption,
        timeDependentRestriction
    }

    enum RoutingType {
        fastest, shortest, balanced
    }

    enum TransportMode {
        car, pedestrian, carHOV, publicTransport, publicTransportTimeTable, truck, bicycle
    }

    enum TrafficMode {
        enabled, disabled
    }

    class ResponseWrapper {
        @JsonProperty("response")
        private Response response;

        public Response getResponse() {
            return response;
        }
    }

    class Response {
        @JsonProperty("route")
        private List<HereApi.Route> route;
        @JsonProperty("metaInfo")
        private MetaInfo metaInfo;
        @JsonProperty("language")
        private Locale language;

        public List<HereApi.Route> getRoute() {
            return route;
        }

        public MetaInfo getMetaInfo() {
            return metaInfo;
        }

        public Locale getLanguage() {
            return language;
        }
    }

    class MetaInfo {
        @JsonProperty("timestamp")
        private OffsetDateTime timestamp;
        @JsonProperty("mapVersion")
        private String mapVersion;
        @JsonProperty("moduleVersion")
        private String moduleVersion;
        @JsonProperty("interfaceVersion")
        private String interfaceVersion;
        @JsonProperty("availableMapVersion")
        private List<String> availableMapVersion;

        public OffsetDateTime getTimestamp() {
            return timestamp;
        }

        public String getMapVersion() {
            return mapVersion;
        }

        public String getModuleVersion() {
            return moduleVersion;
        }

        public String getInterfaceVersion() {
            return interfaceVersion;
        }

        public List<String> getAvailableMapVersion() {
            return availableMapVersion;
        }
    }

    class Route {
        @JsonProperty("summary")
        private Summary summary;
        @JsonProperty("shape")
        @JsonDeserialize(using = LineStringDeserializer.class)
        private LineString shape;
        @JsonProperty("mode")
        private Mode mode;
        @JsonProperty("leg")
        private List<Leg> legs;
        @JsonProperty("waypoint")
        private List<Waypoint> wayPoints;

        public Summary getSummary() {
            return summary;
        }

        public LineString getShape() {
            return shape;
        }

        public Mode getMode() {
            return mode;
        }

        public List<Leg> getLegs() {
            return legs;
        }

        public List<Waypoint> getWayPoints() {
            return wayPoints;
        }
    }

    class Summary {
        @JsonProperty("distance")
        private Long distance;
        @JsonProperty("trafficTime")
        private Long trafficTime;
        @JsonProperty("baseTime")
        private Long baseTime;
        @JsonProperty("flags")
        private List<String> flags;
        @JsonProperty("text")
        private String text;
        @JsonProperty("travelTime")
        private Long travelTime;

        public Long getDistance() {
            return distance;
        }

        public Long getTrafficTime() {
            return trafficTime;
        }

        public Long getBaseTime() {
            return baseTime;
        }

        public Long getTravelTime() {
            return travelTime;
        }

        public BigDecimal getDuration() {
            if (getTrafficTime() != null) return BigDecimal.valueOf(getTrafficTime());
            if (getBaseTime() != null) return BigDecimal.valueOf(getBaseTime());
            if (getTravelTime() != null) return BigDecimal.valueOf(getTravelTime());
            return null;
        }

        public BigDecimal getLength() {
            if (getDistance() == null) return null;
            return BigDecimal.valueOf(getDistance());
        }
    }

    class Leg {
        @JsonProperty("start")
        private Waypoint start;
        @JsonProperty("end")
        private Waypoint end;
        @JsonProperty("length")
        private Long length;
        @JsonProperty("travelTime")
        private Long travelTime;
        @JsonProperty("maneuver")
        private List<Maneuver> maneuvers = new LinkedList<>();

        public List<Maneuver> getManeuvers() {
            return maneuvers;
        }
    }

    class Maneuver {
        @JsonProperty("instruction")
        private String instruction;
        @JsonProperty("travelTime")
        private Long travelTime;
        @JsonProperty("trafficTime")
        private Long trafficTime;
        @JsonProperty("length")
        private Long length;
        @JsonProperty("id")
        private String id;
        @JsonProperty("position")
        @JsonDeserialize(using = PointDeserializer.class)
        private Point position;
        @JsonProperty("shape")
        @JsonDeserialize(using = LineStringDeserializer.class)
        private Geometry shape;
        @JsonProperty("direction")
        private String direction;
        @JsonProperty("roadName")
        private String roadName;


        public BigDecimal getLength() {
            if (this.length == null) return null;
            return BigDecimal.valueOf(this.length);
        }

        public BigDecimal getDuration() {
            if (this.trafficTime != null) return BigDecimal.valueOf(this.trafficTime);
            if (this.travelTime != null) return BigDecimal.valueOf(this.travelTime);
            return null;
        }

        public String getDirection() {
            return direction;
        }

        public String getInstruction() {
            return instruction;
        }

        public BigDecimal getMaxHeight() {
            return null;
        }

        public BigDecimal getMaxLoad() {
            return null;
        }

        public Integer getSpeedLimit() {
            return null;
        }

        public SpeedLimitUnit getSpeedLimitUnit() {
            return null;
        }

        public String getRoadName() {
            return roadName;
        }

        public Geometry getShape() {
            return shape;
        }
    }

    class Mode {
        @JsonProperty("type")
        private RoutingType type;
        @JsonProperty("transportModes")
        private List<TransportMode> transportModes = new LinkedList<>();
        @JsonProperty("trafficMode")
        private TrafficMode trafficMode;
        // TODO feature
    }

    class Waypoint {
        @JsonProperty("linkId")
        private String linkId;
        @JsonProperty("mappedPosition")
        @JsonDeserialize(using = PointDeserializer.class)
        private Point mappedPosition;
        @JsonProperty("originalPosition")
        @JsonDeserialize(using = PointDeserializer.class)
        private Point originalPosition;
        @JsonProperty("type")
        private String type;
        @JsonProperty("spot")
        private Double spot;
        @JsonProperty("sideOfStreet")
        private String sideOfStreet;
        @JsonProperty("mappedRoadName")
        private String mappedRoadName;
        @JsonProperty("label")
        private String label;
        @JsonProperty("shapeIndex")
        private Long shapeIndex;
        @JsonProperty("source")
        private String source;

        public String getLinkId() {
            return linkId;
        }

        public Point getMappedPosition() {
            return mappedPosition;
        }

        public Point getOriginalPosition() {
            return originalPosition;
        }

        public String getType() {
            return type;
        }

        public Double getSpot() {
            return spot;
        }

        public String getSideOfStreet() {
            return sideOfStreet;
        }

        public String getMappedRoadName() {
            return mappedRoadName;
        }

        public String getLabel() {
            return label;
        }

        public Long getShapeIndex() {
            return shapeIndex;
        }

        public String getSource() {
            return source;
        }
    }

    class PointDeserializer extends JsonDeserializer<Point> {
        private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);

        @Override
        public Point deserialize(JsonParser p, DeserializationContext context) throws IOException {
            JsonNode node = p.readValueAs(JsonNode.class);
            double longitude = node.path("longitude").doubleValue();
            double latitude = node.path("latitude").doubleValue();
            return geometryFactory.createPoint(new Coordinate(longitude, latitude));
        }
    }

    class LineStringDeserializer extends JsonDeserializer<Geometry> {
        private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);

        @Override
        public Geometry deserialize(JsonParser p, DeserializationContext context) throws IOException {
            Coordinate[] coordinates = Arrays.stream(p.readValueAs(String[].class))
                    .filter(x -> x != null && !x.isEmpty())
                    .map(this::fromString)
                    .toArray(Coordinate[]::new);

            if (coordinates.length == 1) {
                return geometryFactory.createPoint(coordinates[0]);
            }
            return geometryFactory.createLineString(coordinates);
        }

        @NotNull
        private Coordinate fromString(@NotNull String value) {
            String[] split = value.split(",");
            if (split.length < 2) {
                throw new IllegalArgumentException();
            }
            double y = Double.parseDouble(split[0]);
            double x = Double.parseDouble(split[1]);
            if (split.length > 2) {
                double z = Double.parseDouble(split[2]);
                return new Coordinate(x, y, z);
            }
            return new Coordinate(x, y);
        }


    }
}
