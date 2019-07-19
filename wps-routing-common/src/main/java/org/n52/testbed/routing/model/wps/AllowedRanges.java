package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * AllowedRanges
 */
@Validated
public class AllowedRanges implements ValueDefinition {
    @JsonProperty("allowedRanges")
    @Valid
    private List<Range> allowedRanges = new ArrayList<>();

    @NotNull
    @Valid
    public List<Range> getAllowedRanges() {
        return allowedRanges;
    }

    public void setAllowedRanges(List<Range> allowedRanges) {
        this.allowedRanges = allowedRanges;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AllowedRanges that = (AllowedRanges) o;
        return Objects.equals(this.getAllowedRanges(), that.getAllowedRanges());
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowedRanges);
    }
}
