package org.n52.testbed.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.n52.javaps.algorithm.ExecutionException;
import org.n52.testbed.routing.model.routing.Route;
import org.n52.testbed.routing.model.routing.RouteDefinition;
import org.n52.testbed.routing.model.routing.RouteFeature;
import org.n52.testbed.routing.model.wps.Status;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

public abstract class DelegatingRoutingAlgorithm extends AbstractRoutingAlgorithm {

    private static final MediaType APPLICATION_JSON = MediaType.get("application/json");
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
                                                       .retryOnConnectionFailure(true)
                                                       .followRedirects(true)
                                                       .build();
    @Autowired
    private ObjectMapper objectMapper;

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    protected Route computeRoute() throws Exception {
        RouteDefinition definition = createRouteDefinition();
        byte[] body = objectMapper.writeValueAsBytes(definition);
        RequestBody requestBody = RequestBody.create(APPLICATION_JSON, body);
        Request request = new Request.Builder().post(requestBody).url(getDelegatingEndpoint()).build();
        try (Response execute = CLIENT.newCall(request).execute()) {
            if (!execute.isSuccessful() || execute.body() == null) {
                throw new ExecutionException(String.format("API responded with status %d", execute.code()));
            }
            Route route = objectMapper.readValue(execute.body().charStream(), Route.class);

            // add the status if not set
            if (route.getStatus() == null) {
                route.setStatus(Status.SUCCESSFUL);
            }
            route.setName(getName());
            // remove any links
            route.setLinks(null);

            // calculate the bounding box if not present
            if ((route.getBbox() == null || route.getBbox().isEmpty())
                && route.getFeatures() != null && !route.getFeatures().isEmpty()) {
                Envelope envelope = new Envelope();
                route.getFeatures().stream()
                     .map(RouteFeature::getGeometry)
                     .filter(Objects::nonNull)
                     .map(Geometry::getEnvelopeInternal)
                     .forEach(envelope::expandToInclude);
                route.setBbox(Arrays.asList(BigDecimal.valueOf(envelope.getMinX()),
                                            BigDecimal.valueOf(envelope.getMinY()),
                                            BigDecimal.valueOf(envelope.getMaxX()),
                                            BigDecimal.valueOf(envelope.getMaxY())));
            }
            return route;
        }
    }

    protected abstract RouteDefinition createRouteDefinition();

    protected abstract String getDelegatingEndpoint();

}
