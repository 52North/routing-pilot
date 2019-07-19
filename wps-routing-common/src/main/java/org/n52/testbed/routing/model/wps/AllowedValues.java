package org.n52.testbed.routing.model.wps;

import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Objects;

/**
 * AllowedValues
 */
@Validated
public class AllowedValues extends ArrayList<Object> implements ValueDefinition {

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

}
