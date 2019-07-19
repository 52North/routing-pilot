package org.n52.testbed.routing.persistence;

import org.bson.types.ObjectId;
import org.n52.testbed.routing.model.routing.Route;
import org.n52.testbed.routing.model.routing.RouteDefinition;
import org.n52.testbed.routing.model.routing.RouteInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Optional;


@Document
public class MongoRoute implements RouteInfo {

    @Id
    private ObjectId id = new ObjectId();
    private RouteDefinition definition;
    private Route route;
    @NotEmpty
    private String processId;
    @NotEmpty
    private String jobId;
    @Indexed(sparse = true)
    private URI subscriber;

    public MongoRoute(@NotEmpty String processId, @NotEmpty String jobId) {
        this.processId = processId;
        this.jobId = jobId;
    }

    @NotNull
    public ObjectId getId() {
        return id;
    }

    public void setId(@NotNull ObjectId id) {
        this.id = id;
    }

    @NotEmpty
    public String getJobId() {
        return jobId;
    }

    public void setJobId(@NotEmpty String jobId) {
        this.jobId = jobId;
    }

    public RouteDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(RouteDefinition definition) {
        this.definition = definition;
    }

    @NotEmpty
    public String getProcessId() {
        return processId;
    }

    public void setProcessId(@NotEmpty String processId) {
        this.processId = processId;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public URI getSubscriber() {
        return subscriber;
    }

    public boolean hasSubscriber() {
        return this.subscriber != null;
    }

    public void setSubscriber(URI subscriber) {
        this.subscriber = subscriber;
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return getId().toString();
    }

    @NotNull
    @Override
    public Optional<String> getTitle() {
        return Optional.ofNullable(getDefinition().getName()).filter(x -> !x.isEmpty());
    }
}
