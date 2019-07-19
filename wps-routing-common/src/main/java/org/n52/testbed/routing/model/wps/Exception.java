package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Exception
 */
@Validated
public class Exception {
    @JsonProperty("code")
    private String code = null;

    @JsonProperty("description")
    private String description = null;

    public Exception code(String code) {
        this.code = code;
        return this;
    }

    @NotNull
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Exception description(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Exception exception = (Exception) o;
        return Objects.equals(this.code, exception.code) &&
                Objects.equals(this.description, exception.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, description);
    }

}
