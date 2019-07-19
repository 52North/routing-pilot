package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.util.Objects;

/**
 * ValuesReference
 */
@Validated
public class ValuesReference implements ValueDefinition {

    @JsonValue
    private URI uri;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValuesReference)) return false;
        ValuesReference that = (ValuesReference) o;
        return Objects.equals(getUri(), that.getUri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUri());
    }

}
