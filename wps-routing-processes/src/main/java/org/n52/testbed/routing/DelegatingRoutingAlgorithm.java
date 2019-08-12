package org.n52.testbed.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.n52.javaps.algorithm.ExecutionException;
import org.n52.testbed.routing.model.routing.Route;
import org.n52.testbed.routing.model.routing.RouteDefinition;
import org.springframework.beans.factory.annotation.Autowired;

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
            return objectMapper.readValue(execute.body().charStream(), Route.class);
        }
    }

    protected abstract RouteDefinition createRouteDefinition();

    protected abstract String getDelegatingEndpoint();

}
