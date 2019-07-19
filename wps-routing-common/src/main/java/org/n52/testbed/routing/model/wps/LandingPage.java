package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.n52.testbed.routing.model.Link;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * LandingPage
 */
@Validated
public class LandingPage {
    @JsonProperty("links")
    @Valid
    private List<Link> links = new ArrayList<>();

    @NotNull
    @Valid
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LandingPage that = (LandingPage) o;
        return Objects.equals(this.getLinks(), that.getLinks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLinks());
    }

}
