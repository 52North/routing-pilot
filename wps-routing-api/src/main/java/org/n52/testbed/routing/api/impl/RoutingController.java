/*
 * Copyright 2019 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.testbed.routing.api.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.n52.testbed.routing.api.DefaultApi;
import org.n52.testbed.routing.api.Mode;
import org.n52.testbed.routing.model.ConformanceClasses;
import org.n52.testbed.routing.model.Link;
import org.n52.testbed.routing.model.LinkRelation;
import org.n52.testbed.routing.model.MediaTypes;
import org.n52.testbed.routing.model.routing.Route;
import org.n52.testbed.routing.model.routing.RouteDefinition;
import org.n52.testbed.routing.model.routing.RouteInfo;
import org.n52.testbed.routing.model.routing.Routes;
import org.n52.testbed.routing.model.wps.ComplexDataTypeDescription;
import org.n52.testbed.routing.model.wps.ConfClasses;
import org.n52.testbed.routing.model.wps.Execute;
import org.n52.testbed.routing.model.wps.Format;
import org.n52.testbed.routing.model.wps.FormatDescription;
import org.n52.testbed.routing.model.wps.InputDescription;
import org.n52.testbed.routing.model.wps.Inputs;
import org.n52.testbed.routing.model.wps.JobCollection;
import org.n52.testbed.routing.model.wps.LandingPage;
import org.n52.testbed.routing.model.wps.Process;
import org.n52.testbed.routing.model.wps.ProcessCollection;
import org.n52.testbed.routing.model.wps.ProcessOffering;
import org.n52.testbed.routing.model.wps.Result;
import org.n52.testbed.routing.model.wps.StatusInfo;
import org.n52.testbed.routing.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import retrofit2.Response;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Controller to delegate the processing API requests to a WPS.
 */
@Controller
@Scope("request")
public class RoutingController extends AbstractRoutingController implements DefaultApi {
    private static final String DATA_TITLE = "The routes.";
    private static final String SELF_TITLE = "this document";
    private static final String SERVICE_TITLE = "The API definition";
    private static final String PROCESSES_TITLE = "The processes offered by this server";
    private static final String CONFORMANCE_TITLE = "WPS 2.0 REST/JSON Binding Extension "
                                                    + "conformance classes implemented by this server";
    private static final String OPEN_API_URL = "https://app.swaggerhub.com/apiproxy/schema/file/apis/cportele/wps-routing-api/1.0.0?format=json";


    @Autowired
    private OkHttpClient client;

    @Autowired
    private RouteService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResponseEntity<Route> computeRoute(@Valid RouteDefinition routeDefinition, @Valid Mode mode) {
        switch (Optional.ofNullable(mode).orElse(Mode.ASYNC)) {
            case ASYNC:
                RouteInfo routeInfo = service.createRouteAsync(routeDefinition);
                return created(getUriBuilder().path("/routes/{routeId}").build(routeInfo.getIdentifier()));
            case SYNC:
                Route route = service.createRouteSync(routeDefinition);
                return ResponseEntity.ok(route);
            default:
                throw new HttpStatusError(HttpStatus.BAD_REQUEST, "unsupported mode");
        }
    }

    @Override
    public ResponseEntity<Void> deleteRoute(String routeId) {
        service.deleteRoute(routeId);
        return noContent();
    }

    @Override
    public ResponseEntity<Route> getRoute(String routeId) {
        Route route = service.getRoute(routeId);
        route.setLinks(getRouteLinks(routeId));
        return ResponseEntity.ok(route);
    }

    @NotNull
    private List<Link> getRouteLinks(@NotNull String routeId) {
        URI definitionURI = getUriBuilder().path("/routes/{routeId}/definition").build(routeId);
        return Arrays.asList(new Link().rel(LinkRelation.SELF)
                                       .type(MediaTypes.APPLICATION_GEO_JSON)
                                       .href(createRouteLink(routeId).toASCIIString()),
                             new Link().rel(LinkRelation.DESCRIBED_BY)
                                       .type(MediaTypes.APPLICATION_JSON)
                                       .href(definitionURI.toASCIIString()));
    }

    @Override
    public ResponseEntity<RouteDefinition> getRouteDefinition(String routeId) {
        return ResponseEntity.ok(service.getRouteDefinition(routeId));
    }

    @Override
    public ResponseEntity<Routes> getRoutes() {
        Routes routes = new Routes();
        routes.setLinks(Stream.concat(Stream.of(getRoutesSelfLink()),
                                      service.getRoutes().map(this::getRouteItemLink)).collect(toList()));
        return ResponseEntity.ok(routes);
    }

    @NotNull
    private Link getRoutesSelfLink() {
        return new Link().rel(LinkRelation.SELF)
                         .href(getUriBuilder().path("/routes").toUriString())
                         .type(MediaTypes.APPLICATION_JSON)
                         .title(SELF_TITLE);
    }

    @NotNull
    private Link getRouteItemLink(@NotNull RouteInfo route) {
        Link link = new Link().rel(LinkRelation.ITEM).type(MediaTypes.APPLICATION_GEO_JSON);
        route.getTitle().ifPresent(link::setTitle);
        link.href(createRouteLink(route.getIdentifier()).toASCIIString());
        return link;
    }

    private URI createRouteLink(@NotNull String routeId) {
        return getUriBuilder().path("/routes/{routeId}").build(routeId);
    }

    @Override
    public ResponseEntity<Void> execute(@Valid Execute body) {
        RouteInfo routeInfo = service.createRouteAsync(body);
        return created(getUriBuilder().path("/processes/routing/jobs/{jobId}").build(routeInfo.getJobId()));
    }

    @Override
    public ResponseEntity<JobCollection> getJobList() {
        JobCollection jobCollection = new JobCollection();
        jobCollection.setJobs(service.getRoutes().map(RouteInfo::getJobId).filter(Objects::nonNull).collect(toList()));
        return ResponseEntity.ok(jobCollection);
    }

    @Override
    public ResponseEntity<ProcessOffering> getProcessDescription() {
        try {
            Response<ProcessOffering> response = getOgcProcessingApi()
                                                         .getProcessDescription(getRoutingProcessId())
                                                         .execute();
            if (!response.isSuccessful()) {
                return copyFailure(response);
            }
            ProcessOffering description = response.body();
            if (description == null || description.getProcess() == null) {
                return ResponseEntity.notFound().build();
            }

            Process process = description.getProcess();
            // replace the process id with the canonical one
            process.setId(ROUTING_PROCESS_ID);
            // remove the start, end and intermediate inputs and replace them with the canonical waypoints input
            process.setInputs(Stream.concat(
                    Stream.of(createWaypointsDescription()),
                    process.getInputs().stream()
                           .filter(x -> !x.getId().equals(Inputs.START))
                           .filter(x -> !x.getId().equals(Inputs.END))
                           .filter(x -> !x.getId().equals(Inputs.INTERMEDIATES)))
                                    .collect(toList()));
            // replace the links with links to this service
            process.getLinks().forEach(link -> {
                if (link.getRel().equals(LinkRelation.CANONICAL)) {
                    link.setHref(getUriBuilder().path("/processes/{processId}/jobs")
                                                .build(ROUTING_PROCESS_ID)
                                                .toASCIIString());
                }
            });
            return ResponseEntity.ok(description);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private InputDescription createWaypointsDescription() {
        FormatDescription formatDescription = new FormatDescription();
        formatDescription.setDefaultValue(true);
        formatDescription.setFormat(new Format(MediaTypes.APPLICATION_GEO_JSON));
        ComplexDataTypeDescription input = new ComplexDataTypeDescription();
        input.setFormats(Collections.singletonList(formatDescription));
        InputDescription inputDescription = new InputDescription();
        inputDescription.setId(Inputs.WAYPOINTS);
        inputDescription.setInput(input);
        inputDescription.setMinOccurs(0);
        inputDescription.setMaxOccurs(1);
        inputDescription.setTitle("The route waypoints");
        inputDescription.setDescription("A list of points along the route. At least two "
                                        + "points have to be provided (start and end point).");
        return inputDescription;
    }

    @Override
    public ResponseEntity<ProcessCollection> getProcesses() {
        try {
            Response<ProcessCollection> response = HttpStatusError.throwIfNotSuccessful(getOgcProcessingApi()
                                                                                                .getProcesses()
                                                                                                .execute());
            ProcessCollection processCollection = new ProcessCollection();
            Optional.ofNullable(response.body())
                    .map(ProcessCollection::getProcesses)
                    .map(List::stream)
                    .map(stream -> stream.filter(process -> process.getId().equals(getRoutingProcessId()))
                                         .peek(process -> {
                                             process.setId(ROUTING_PROCESS_ID);
                                             process.getLinks().forEach(link -> {
                                                 if (link.getRel().equals(LinkRelation.CANONICAL)) {
                                                     link.setHref(getProcessUri(ROUTING_PROCESS_ID));
                                                 }
                                             });
                                         }).collect(toList()))
                    .ifPresent(processCollection::setProcesses);
            return ResponseEntity.ok(processCollection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the URI of the process.
     *
     * @param processId The identifier of the process.
     * @return The URI.
     */
    private String getProcessUri(String processId) {
        return getUriBuilder().path("/processes/{processId}").build(processId).toString();
    }

    @Override
    public ResponseEntity<Result> getResult(String jobId) {
        try {
            Response<Result> response = getOgcProcessingApi().getResult(getRoutingProcessId(), jobId).execute();
            if (!response.isSuccessful()) {
                return copyFailure(response);
            }
            return ResponseEntity.ok(response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<StatusInfo> getStatus(String jobId) {
        Route routeByJobId = service.getRouteByJobId(jobId);
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setStatus(routeByJobId.getStatus());
        return ResponseEntity.ok(statusInfo);
    }

    @Override
    public ResponseEntity<LandingPage> getLandingPage() {
        LandingPage landingPage = new LandingPage();
        landingPage.setLinks(Arrays.asList(getSelfLink(),
                                           getServiceLink(),
                                           getConformanceLink(),
                                           getProcessesLink(),
                                           getDataLink()));
        return ResponseEntity.ok(landingPage);
    }

    private Link getSelfLink() {
        Link link = new Link();
        link.rel(LinkRelation.SELF);
        link.type(MediaTypes.APPLICATION_JSON);
        link.title(SELF_TITLE);
        link.href(getUriBuilder().toUriString());
        return link;
    }

    private Link getServiceLink() {
        Link link = new Link();
        link.rel(LinkRelation.SERVICE);
        link.type(MediaTypes.APPLICATION_OPENAPI);
        link.title(SERVICE_TITLE);
        link.href(getUriBuilder().path("/api").toUriString());
        return link;
    }

    private Link getConformanceLink() {
        Link link = new Link();
        link.rel(LinkRelation.CONFORMANCE);
        link.type(MediaTypes.APPLICATION_JSON);
        link.title(CONFORMANCE_TITLE);
        link.href(getUriBuilder().path("/conformance").toUriString());
        return link;
    }

    private Link getProcessesLink() {
        Link link = new Link();
        link.rel(LinkRelation.PROCESSES);
        link.type(MediaTypes.APPLICATION_JSON);
        link.title(PROCESSES_TITLE);
        link.href(getUriBuilder().path("/processes").toUriString());
        return link;
    }

    private Link getDataLink() {
        Link link = new Link();
        link.rel(LinkRelation.DATA);
        link.type(MediaTypes.APPLICATION_JSON);
        link.title(DATA_TITLE);
        link.href(getUriBuilder().path("/routes").toUriString());
        return link;
    }

    @Override
    public ResponseEntity<ConfClasses> getConformanceDeclaration() {

        try {
            Set<String> inputIds = getProcessInputs();

            ConfClasses confClasses = new ConfClasses();
            confClasses.addConformsToItem(ConformanceClasses.PROCESSES_API);
            confClasses.addConformsToItem(ConformanceClasses.CORE);
            confClasses.addConformsToItem(ConformanceClasses.CALLBACK);
            confClasses.addConformsToItem(ConformanceClasses.DELETE_ROUTE);
            //confClasses.addConformsToItem(ConformanceClasses.RESULT_SET);
            confClasses.addConformsToItem(ConformanceClasses.SYNC_MODE);

            if (inputIds.contains(Inputs.ALGORITHM)) {
                confClasses.addConformsToItem(ConformanceClasses.ROUTING_ALGORITHM);
            }
            if (inputIds.contains(Inputs.DATASET)) {
                confClasses.addConformsToItem(ConformanceClasses.SOURCE_DATASET);
            }
            if (inputIds.contains(Inputs.ENGINE)) {
                confClasses.addConformsToItem(ConformanceClasses.ROUTING_ENGINE);
            }
            if (inputIds.contains(Inputs.MAX_HEIGHT)) {
                confClasses.addConformsToItem(ConformanceClasses.MAX_HEIGHT);
            }
            if (inputIds.contains(Inputs.MAX_WEIGHT)) {
                confClasses.addConformsToItem(ConformanceClasses.MAX_WEIGHT);
            }
            if (inputIds.contains(Inputs.OBSTACLES)) {
                confClasses.addConformsToItem(ConformanceClasses.OBSTACLES);
            }
            if (inputIds.contains(Inputs.WHEN)) {
                confClasses.addConformsToItem(ConformanceClasses.TIME);
            }
            if (inputIds.contains(Inputs.INTERMEDIATES)) {
                confClasses.addConformsToItem(ConformanceClasses.INTERMEDIATE_WAYPOINTS);
            }

            return ResponseEntity.ok(confClasses);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonNode getOpenApiDefinition() {
        Request request = new Request.Builder().get().url(OPEN_API_URL).build();
        try (okhttp3.Response execute = client.newCall(request).execute()) {
            HttpStatusError.throwIfNotSuccessful(execute);
            try (Reader reader = execute.body().charStream()) {
                ObjectNode jsonNode = objectMapper.readValue(reader, ObjectNode.class);
                ObjectNode server = jsonNode.withArray("servers").addObject();
                server.put("description", "This instance.");
                server.put("url", getUriBuilder().toUriString());
                return jsonNode;
            }
        } catch (IOException e) {
            throw new HttpStatusError(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private Set<String> getProcessInputs() throws IOException {
        Response<ProcessOffering> response = getOgcProcessingApi().getProcessDescription(getRoutingProcessId())
                                                                  .execute();
        ProcessOffering processOffering = HttpStatusError.throwIfNotSuccessful(response).body();
        return processOffering.getProcess().getInputs().stream().map(InputDescription::getId).collect(toSet());
    }

}
