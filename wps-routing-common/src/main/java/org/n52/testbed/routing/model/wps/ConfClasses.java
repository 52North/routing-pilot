package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ConfClasses
 */
@Validated
public class ConfClasses {
    @JsonProperty("conformsTo")
    @Valid
    private List<String> conformsTo = new ArrayList<>();

    public ConfClasses conformsTo(List<String> conformsTo) {
        this.conformsTo = conformsTo;
        return this;
    }

    public ConfClasses addConformsToItem(String conformsToItem) {
        this.conformsTo.add(conformsToItem);
        return this;
    }

    @NotNull
    public List<String> getConformsTo() {
        return conformsTo;
    }

    public void setConformsTo(List<String> conformsTo) {
        this.conformsTo = conformsTo;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfClasses confClasses = (ConfClasses) o;
        return Objects.equals(this.conformsTo, confClasses.conformsTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conformsTo);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ConfClasses {\n");

        sb.append("    conformsTo: ").append(toIndentedString(conformsTo)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
