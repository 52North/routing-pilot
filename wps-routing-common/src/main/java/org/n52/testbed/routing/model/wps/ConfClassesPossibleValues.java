package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfClassesPossibleValues {
    @JsonProperty("values")
    private List<String> values = new ArrayList<>();

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = Optional.ofNullable(values).orElseGet(ArrayList::new);
    }

    public ConfClassesPossibleValues() {}

    public ConfClassesPossibleValues(List<String> values) {
        this.values = Optional.ofNullable(values).orElseGet(ArrayList::new);
    }
}
