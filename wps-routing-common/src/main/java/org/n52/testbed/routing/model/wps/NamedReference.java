package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * NamedReference
 */
@Validated
public class NamedReference {
    @JsonProperty("name")
    private String name;

    @JsonProperty("reference")
    private String reference;

    public NamedReference() {
        this(null, null);
    }

    public NamedReference(String name) {
        this(name, null);
    }

    public NamedReference(String name, String reference) {
        this.name = name;
        this.reference = reference;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NamedReference that = (NamedReference) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getReference(), that.getReference());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getReference());
    }

}
