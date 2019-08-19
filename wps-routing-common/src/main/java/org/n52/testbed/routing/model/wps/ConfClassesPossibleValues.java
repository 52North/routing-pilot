package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.Collection;

@JsonTypeName(value = "values")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class ConfClassesPossibleValues extends ArrayList<String> {

    public ConfClassesPossibleValues(int initialCapacity) {
        super(initialCapacity);
    }

    public ConfClassesPossibleValues() {
    }

    public ConfClassesPossibleValues(Collection<? extends String> c) {
        super(c);
    }
}
