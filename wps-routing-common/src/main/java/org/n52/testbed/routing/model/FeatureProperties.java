package org.n52.testbed.routing.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FeatureProperties<T extends FeatureProperties<T>> {
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnySetter
    public T setAdditionalProperty(String key, Object value) {
        this.additionalProperties.put(key, value);
        return self();
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <X> X getAdditionalProperty(String key) {
        return (X) this.additionalProperties.get(key);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return Collections.unmodifiableMap(this.additionalProperties);
    }
}


