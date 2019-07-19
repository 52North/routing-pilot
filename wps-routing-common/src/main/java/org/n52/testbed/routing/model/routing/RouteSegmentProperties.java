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
package org.n52.testbed.routing.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * RouteSegmentProperties
 */
@Validated
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteSegmentProperties extends RouteFeatureProperties<RouteSegmentProperties> {

    public static final String LEVEL_OF_DETAIL = "levelOfDetail";
    public static final String LENGTH = "length_m";
    public static final String DURATION = "duration_s";
    public static final String MAX_HEIGHT = "maxHeight_m";
    public static final String MAX_LOAD = "maxLoad_t";
    public static final String SPEED_LIMIT = "speedLimit";
    public static final String SPEED_LIMIT_UNIT = "speedLimitUnit";
    public static final String ROAD_NAME = "roadName";
    public static final String INSTRUCTIONS = "instructions";
    @JsonProperty(LEVEL_OF_DETAIL)
    private LevelOfDetail levelOfDetail;

    @JsonProperty(LENGTH)
    private BigDecimal length;

    @JsonProperty(DURATION)
    private BigDecimal duration;

    @JsonProperty(MAX_HEIGHT)
    private BigDecimal maxHeight;

    @JsonProperty(MAX_LOAD)
    private BigDecimal maxLoad;

    @JsonProperty(SPEED_LIMIT)
    private Integer speedLimit;

    @JsonProperty(SPEED_LIMIT_UNIT)
    private SpeedLimitUnit speedLimitUnit;

    @JsonProperty(ROAD_NAME)
    private String roadName;

    @JsonProperty(INSTRUCTIONS)
    private Instruction instructions;

    @Override
    public RouteFeatureType getType() {
        return RouteFeatureType.SEGMENT;
    }

    public RouteSegmentProperties levelOfDetail(LevelOfDetail levelOfDetail) {
        this.levelOfDetail = levelOfDetail;
        return self();
    }

    @NotNull
    public LevelOfDetail getLevelOfDetail() {
        return levelOfDetail;
    }

    public void setLevelOfDetail(LevelOfDetail levelOfDetail) {
        this.levelOfDetail = levelOfDetail;
    }

    public RouteSegmentProperties length(BigDecimal lengthM) {
        this.length = lengthM;
        return self();
    }

    @NotNull
    @Valid
    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public RouteSegmentProperties duration(BigDecimal durationS) {
        this.duration = durationS;
        return self();
    }

    @NotNull
    @Valid
    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public RouteSegmentProperties maxHeight(BigDecimal maxHeight) {
        this.maxHeight = maxHeight;
        return self();
    }

    @Valid
    public BigDecimal getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(BigDecimal maxHeight) {
        this.maxHeight = maxHeight;
    }

    public RouteSegmentProperties maxLoadT(BigDecimal maxLoadT) {
        this.maxLoad = maxLoadT;
        return self();
    }

    @Valid
    public BigDecimal getMaxLoad() {
        return maxLoad;
    }

    public void setMaxLoad(BigDecimal maxLoad) {
        this.maxLoad = maxLoad;
    }

    public RouteSegmentProperties speedLimit(Integer speedLimit) {
        this.speedLimit = speedLimit;
        return self();
    }

    public Integer getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Integer speedLimit) {
        this.speedLimit = speedLimit;
    }

    public RouteSegmentProperties speedLimitUnit(SpeedLimitUnit speedLimitUnit) {
        this.speedLimitUnit = speedLimitUnit;
        return self();
    }

    public SpeedLimitUnit getSpeedLimitUnit() {
        return speedLimitUnit;
    }

    public void setSpeedLimitUnit(SpeedLimitUnit speedLimitUnit) {
        this.speedLimitUnit = speedLimitUnit;
    }

    public RouteSegmentProperties roadName(String roadName) {
        this.roadName = roadName;
        return self();
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public RouteSegmentProperties instructions(Instruction instructions) {
        this.instructions = instructions;
        return self();
    }

    public Instruction getInstructions() {
        return instructions;
    }

    public void setInstructions(Instruction instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RouteSegmentProperties routesegmentProperties = (RouteSegmentProperties) o;
        return Objects.equals(this.getType(), routesegmentProperties.getType()) &&
                Objects.equals(this.levelOfDetail, routesegmentProperties.levelOfDetail) &&
                Objects.equals(this.length, routesegmentProperties.length) &&
                Objects.equals(this.duration, routesegmentProperties.duration) &&
                Objects.equals(this.maxHeight, routesegmentProperties.maxHeight) &&
                Objects.equals(this.maxLoad, routesegmentProperties.maxLoad) &&
                Objects.equals(this.speedLimit, routesegmentProperties.speedLimit) &&
                Objects.equals(this.speedLimitUnit, routesegmentProperties.speedLimitUnit) &&
                Objects.equals(this.roadName, routesegmentProperties.roadName) &&
                Objects.equals(this.instructions, routesegmentProperties.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), levelOfDetail, length, duration, maxHeight, maxLoad,
                            speedLimit, speedLimitUnit, roadName, instructions);
    }
}
