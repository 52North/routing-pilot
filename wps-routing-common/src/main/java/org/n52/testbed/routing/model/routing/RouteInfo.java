package org.n52.testbed.routing.model.routing;

import java.util.Optional;

public interface RouteInfo {
    String getIdentifier();

    Optional<String> getTitle();
}
