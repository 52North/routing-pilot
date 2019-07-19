package org.n52.testbed.routing.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.validation.annotation.Validated;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

/**
 * RouteStartOrEndProperties
 */
@Validated
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteStartOrEndProperties extends RouteFeatureProperties<RouteStartOrEndProperties> {

    @NotNull
    private RouteFeatureType type;

    public RouteStartOrEndProperties() {
        this(null);
    }

    public RouteStartOrEndProperties(@NotNull RouteFeatureType type) {
        this.type = type;
    }

    @JsonSetter()
    public void setType(RouteFeatureType type) {
        if (type != RouteFeatureType.START && type != RouteFeatureType.END) {
            throw new ValidationException();
        }
        this.type = type;
    }

    public RouteStartOrEndProperties type(RouteFeatureType type) {
        setType(type);
        return self();
    }

    @Override
    public RouteFeatureType getType() {
        return this.type;
    }
}
