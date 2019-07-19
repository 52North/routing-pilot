package org.n52.testbed.routing.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.n52.testbed.routing.client.OgcProcessingApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.util.UriComponentsBuilder;
import retrofit2.Response;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;

public abstract class AbstractRoutingController {
    protected static final String ROUTING_PROCESS_ID = "routing";
    private String routingProcessId;
    private OgcProcessingApi ogcProcessingApi;
    private UriComponentsBuilder uriBuilder;
    private NativeWebRequest request;

    /**
     * The {@link OgcProcessingApi}.
     *
     * @param ogcProcessingApi The {@link OgcProcessingApi}.
     */
    @Autowired
    public void setOgcProcessingApi(OgcProcessingApi ogcProcessingApi) {
        this.ogcProcessingApi = ogcProcessingApi;
    }

    /**
     * The {@link UriComponentsBuilder}.
     *
     * @param uriBuilder The {@link UriComponentsBuilder}.
     */
    @Autowired
    public void setUriBuilder(UriComponentsBuilder uriBuilder) {
        this.uriBuilder = uriBuilder;
    }

    /**
     * The identifier of the routing process in the WPS to delegate to.
     *
     * @param routingProcessId the identifier
     */
    @Value("${routing.process}")
    public void setRoutingProcessId(String routingProcessId) {
        this.routingProcessId = routingProcessId;
    }

    /**
     * The {@link NativeWebRequest}.
     *
     * @param request The {@link NativeWebRequest}.
     */
    @Autowired(required = false)
    public void setRequest(NativeWebRequest request) {
        this.request = request;
    }

    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(this.request).map(x -> x.getNativeRequest(HttpServletRequest.class));
    }

    protected UriComponentsBuilder getUriBuilder() {
        return this.uriBuilder.cloneBuilder();
    }

    protected OgcProcessingApi getOgcProcessingApi() {
        return this.ogcProcessingApi;
    }

    protected String getRoutingProcessId() {
        return this.routingProcessId;
    }

    protected static <T> ResponseEntity<T> created(URI location) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, location.toASCIIString())
                .build();
    }

    protected static <T> ResponseEntity<T> copyFailure(Response<T> response) {
        return ResponseEntity.status(response.code()).build();
    }

    protected static <T> ResponseEntity<T> notFound() {
        return ResponseEntity.notFound().build();
    }

    protected static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }
}
