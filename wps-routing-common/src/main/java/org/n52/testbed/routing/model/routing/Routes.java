package org.n52.testbed.routing.model.routing;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.n52.testbed.routing.model.Link;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The list of routes currently available.  To be discussed. This could be more sophisticated and include information about the status, start/end point, etc. But it is unclear whether this operation is needed and this should be discussed first.
 */
@Validated
public class Routes {
    @JsonProperty("links")
    @Valid
    private List<Link> links;

    public Routes() {
        this(new ArrayList<>());
    }

    public Routes(@Valid List<Link> links) {
        this.links = links;
    }

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
        Routes routes = (Routes) o;
        return Objects.equals(this.links, routes.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links);
    }
}
