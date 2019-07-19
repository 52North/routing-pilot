package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ComplexDataTypeDescription
 */
@Validated
public class ComplexDataTypeDescription implements DataTypeDescription {
    @JsonProperty("formats")
    @Valid
    private List<FormatDescription> formats = new ArrayList<>();

    @NotNull
    @Valid
    public List<FormatDescription> getFormats() {
        return formats;
    }

    public void setFormats(List<FormatDescription> formats) {
        this.formats = formats;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexDataTypeDescription that = (ComplexDataTypeDescription) o;
        return Objects.equals(this.getFormats(), that.getFormats());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFormats());
    }

}
