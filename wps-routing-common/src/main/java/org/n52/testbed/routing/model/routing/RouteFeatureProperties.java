package org.n52.testbed.routing.model.routing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.n52.testbed.routing.model.FeatureProperties;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RouteOverviewProperties.class, name = "overview"),
        @JsonSubTypes.Type(value = RouteSegmentProperties.class, name = "segment"),
        @JsonSubTypes.Type(value = RouteStartOrEndProperties.class, name = "start"),
        @JsonSubTypes.Type(value = RouteStartOrEndProperties.class, name = "end")
})
public abstract class RouteFeatureProperties<T extends RouteFeatureProperties<T>> extends FeatureProperties<T> {

    public static final String TYPE = "type";

    @NotNull
    @JsonProperty(TYPE)
    public abstract RouteFeatureType getType();

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        T properties = (T) o;
        return Objects.equals(getType(), properties.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }

}
