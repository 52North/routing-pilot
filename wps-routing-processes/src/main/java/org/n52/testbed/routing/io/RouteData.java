package org.n52.testbed.routing.io;

import org.n52.javaps.io.complex.ComplexData;
import org.n52.testbed.routing.model.routing.Route;

public class RouteData implements ComplexData<Route> {
    private final Route payload;

    public RouteData(Route payload) {
        this.payload = payload;
    }

    @Override
    public Route getPayload() {
        return this.payload;
    }

    @Override
    public Class<?> getSupportedClass() {
        return Route.class;
    }
}
