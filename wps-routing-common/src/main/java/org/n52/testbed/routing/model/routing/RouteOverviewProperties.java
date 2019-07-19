package org.n52.testbed.routing.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * RouteOverviewProperties
 */
@Validated
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteOverviewProperties extends RouteFeatureProperties<RouteOverviewProperties> {
    public static final String LENGTH = "length_m";
    public static final String DURATION = "duration_s";
    public static final String MAX_HEIGHT = "maxHeight_m";
    public static final String MAX_LOAD = "maxLoad_t";
    public static final String OBSTACLES = "obstacles";

    @JsonProperty(LENGTH)
    private BigDecimal length = null;

    @JsonProperty(DURATION)
    private BigDecimal duration = null;

    @JsonProperty(MAX_HEIGHT)
    private BigDecimal maxHeight = null;

    @JsonProperty(MAX_LOAD)
    private BigDecimal maxLoad = null;

    @JsonProperty(OBSTACLES)
    private String obstacles = null;

    @Override
    public RouteFeatureType getType() {
        return RouteFeatureType.OVERVIEW;
    }

    public RouteOverviewProperties length(BigDecimal lengthM) {
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

    public RouteOverviewProperties duration(BigDecimal duration) {
        this.duration = duration;
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

    public RouteOverviewProperties maxHeight(BigDecimal maxHeightM) {
        this.maxHeight = maxHeightM;
        return self();
    }

    @Valid
    public BigDecimal getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(BigDecimal maxHeight) {
        this.maxHeight = maxHeight;
    }

    public RouteOverviewProperties maxLoad(BigDecimal maxLoadT) {
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

    public RouteOverviewProperties obstacles(String obstacles) {
        this.obstacles = obstacles;
        return self();
    }

    public String getObstacles() {
        return obstacles;
    }

    public void setObstacles(String obstacles) {
        this.obstacles = obstacles;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RouteOverviewProperties routeoverviewProperties = (RouteOverviewProperties) o;
        return Objects.equals(this.getType(), routeoverviewProperties.getType()) &&
                Objects.equals(this.length, routeoverviewProperties.length) &&
                Objects.equals(this.duration, routeoverviewProperties.duration) &&
                Objects.equals(this.maxHeight, routeoverviewProperties.maxHeight) &&
                Objects.equals(this.maxLoad, routeoverviewProperties.maxLoad) &&
                Objects.equals(this.obstacles, routeoverviewProperties.obstacles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), length, duration, maxHeight, maxLoad, obstacles);
    }

}