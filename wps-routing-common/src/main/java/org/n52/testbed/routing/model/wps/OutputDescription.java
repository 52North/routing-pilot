package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * OutputDescription
 */
@Validated
public class OutputDescription extends DescriptionType {
    @JsonProperty("output")
    private DataTypeDescription output = null;

    public DataTypeDescription getOutput() {
        return output;
    }

    public void setOutput(DataTypeDescription output) {
        this.output = output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OutputDescription that = (OutputDescription) o;
        return Objects.equals(this.getOutput(), that.getOutput()) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, super.hashCode());
    }

}
