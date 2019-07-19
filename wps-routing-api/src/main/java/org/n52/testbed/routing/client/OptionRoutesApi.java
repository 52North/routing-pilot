package org.n52.testbed.routing.client;

import org.n52.testbed.routing.model.routing.Route;
import org.n52.testbed.routing.model.routing.RouteDefinition;
import org.n52.testbed.routing.model.routing.Routes;
import retrofit2.Call;
import retrofit2.http.*;

public interface OptionRoutesApi {
    /**
     * compute a route
     * This creates a new route. The payload of the request specifies the definition of the new route.  At a minimum, a route is defined by two &#x60;waypoints&#x60;, the start and end point of the route.  Every API has to support at least &#x27;fastest&#x27; and &#x27;shortest&#x27; as the routing &#x60;preference&#x60;. The default value is &#x27;fastest&#x27;.  An optional &#x60;name&#x60; for the route may be provided. The name will be used as the title in links to the route (e.g., in the response to &#x60;/routes&#x60;) and also included in the route itself.  More parameters and routing constraints can optionally be provided with the routing request: * Source dataset to use when processing the route * Routing engine to use when processing the route * Routing algorithm to use when processing the route * Obstacle requirements * Height restriction * Maximum load restriction * Time of departure or arrival  If the parameter &#x60;mode&#x60; is not provided or has a value &#x27;async&#x27; the response returns a link the new route in the &#x60;Location&#x60; header. If the value is &#x27;sync&#x27; no route resource is created on the server, but the connection is kept open until the route has been computed. The response contains the route. In synchronous mode the &#x60;subscriber&#x60; property is ignored.
     *
     * @param body The definition of the route to compute. (required)
     * @param mode Controls whether the request is processed asynchronuous (the default) or synchronous (the route isreturned in the response to the POST request and not stored on the routing server).  Support for this parameter is not required and the parameter may be removed from the API definition, if conformance class **&#x27;sync-mode&#x27;** is not listed in the conformance declaration under &#x60;/conformance&#x60;. (optional)
     * @return Call&lt;Route&gt;
     */
    @Headers({"Content-Type:application/json"})
    @POST("routes")
    Call<Route> computeRoute(@Body RouteDefinition body, @Path("mode") String mode);

    /**
     * delete a route
     * Delete the route with identifier &#x60;routeId&#x60;.  Note that the WPS API does not define a DELETE operation. But don&#x27;t we need one?
     *
     * @param routeId The id of the route (required)
     * @return Call&lt;Void&gt;
     */
    @DELETE("routes/{routeId}")
    Call<Void> deleteRoute(@Path("routeId") String routeId);

    /**
     * fetch a route
     * The route is represented as a GeoJSON feature collection. Its contents will depend on the &#x60;status&#x60; of the processing.  If the status is &#x27;finished&#x27; the feature collection consists of the following informaton * A &#x60;name&#x60;, if one was provided with the route definition. * A link to the canonical URI of the route and its definition (link relations &#x60;self&#x60; and &#x60;describedBy&#x60;) * An array of features (the properties of each is to be decided)   * The route overview feature. This has a LineString   geometry of the complete route from start to end location.   * The start point of the route with a Point geometry.   * A feature for every segment of the route. This has a   LineString geometry starting at the end of the previous   segment (or, for the first segment, the start point).   * The end point of the route with a Point geometry.  If the status is &#x27;accepted&#x27;, &#x27;running&#x27; or &#x27;failed&#x27; the feature collection has less information: * The route overview has a &#x60;null&#x60; geometry. * No segment features are included.  The parameter &#x60;resultSet&#x60; may be used to request only a subset of the route. * &#x27;full&#x27; (default) returns the complete feature collection representing the route. * &#x27;overview&#x27; returns just the route overview feature. * &#x27;segments&#x27; returns the first segment feature with a link to the second segment (link relation &#x60;next&#x60;), if there is more than one segment. It is up to the server how this is implemented. Options include another parameter to identify the segment by index or temporary, opaque URIs. Every segment except the first and the last segment will include two links (link relations &#x60;prev&#x60; and &#x60;next&#x60;). The last segment will only have a &#x60;prev&#x60; link (unless there is only a single segment in which case there is no &#x60;prev&#x60; link).  Support for the &#x60;resultSet&#x60; parameter is not required and the parameter may be removed from the API definition, if conformance class **&#x27;result-set&#x27;** is not listed in the conformance declaration (&#x60;/conformance&#x60;).
     *
     * @param routeId   The id of the route (required)
     * @param resultSet Request the complete route or only a subset.  Support for this parameter is not required and the parameter may be removed from the API definition, if conformance class **&#x27;result-set&#x27;** is not listed in the conformance declaration under &#x60;/conformance&#x60;. (optional)
     * @return Call&lt;Object&gt;
     */
    @GET("routes/{routeId}")
    Call<Object> getRoute(@Path("routeId") String routeId, @Path("resultSet") String resultSet);

    /**
     * fetch the definition of a route
     * This operation returns the original definition of the route that was submitted when the route was created using POST on &#x60;/routes&#x60;.
     *
     * @param routeId The id of the route (required)
     * @return Call&lt;RouteDefinition&gt;
     */
    @GET("routes/{routeId}/definition")
    Call<RouteDefinition> getRouteDefinition(@Path("routeId") String routeId);

    /**
     * fetch the list of routes
     * The list of all routes currently available on this server.  The response contains a link to each route with the link relation type &#x60;item&#x60;.  If a route has a name, the name will be used in the &#x60;title&#x60;, otherwise the &#x60;title&#x60; will be set by the server.  A link to the canonical URI of this document is included, too, with the link relation type &#x60;self&#x60;.
     *
     * @return Call&lt;Routes&gt;
     */
    @GET("routes")
    Call<Routes> getRoutes();


}
