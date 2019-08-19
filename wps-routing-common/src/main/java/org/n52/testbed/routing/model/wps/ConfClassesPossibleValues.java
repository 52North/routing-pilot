package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.Collection;

@JsonRootName("values")
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
