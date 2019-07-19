package org.n52.testbed.routing.client;

import org.n52.testbed.routing.model.wps.ConfClasses;
import org.n52.testbed.routing.model.wps.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface OgcProcessingApi {
    /**
     * landing page
     * The landing page provides links to the API definition, the conformance statements and to existing or new routing tasks.
     *
     * @return Call&lt;LandingPage&gt;
     */
    @GET
    Call<LandingPage> getLandingPage();

    /**
     * information about standards that this API conforms to
     * list all requirements classes specified in a standard (e.g., OGC API - Processes - Part 1: Core) that the server conforms to
     *
     * @return Call&lt;ConfClasses&gt;
     */
    @GET("conformance")
    Call<ConfClasses> getConformanceDeclaration();

    /**
     * retrieve the processes available
     * The response is a list of processes available on this server. In the Routing API Pilot there is only a single process that is specified.
     *
     * @return Call&lt;ProcessCollection&gt;
     */
    @GET("processes")
    @Headers({"Accept: application/json"})
    Call<ProcessCollection> getProcesses();


    /**
     * retrieve a process description
     *
     * @param processId The id of the process (required)
     * @return Call&lt;ProcessOffering&gt;
     */
    @GET("processes/{processId}")
    @Headers({"Accept: application/json"})
    Call<ProcessOffering> getProcessDescription(@Path("processId") String processId);

    /**
     * execute a process.
     *
     * @param body      Mandatory execute request JSON (required)
     * @param processId The id of the process (required)
     * @return Call&lt;Void&gt;
     */
    @POST("processes/{processId}/jobs")
    @Headers({"Content-Type: application/json"})
    Call<Void> execute(@Path("processId") String processId, @Body Execute body);

    /**
     * retrieve the list of jobs for a process.
     *
     * @param processId The id of the process (required)
     * @return Call&lt;JobCollection&gt;
     */
    @GET("processes/{processId}/jobs")
    @Headers({"Accept: application/json"})
    Call<JobCollection> getJobList(@Path("processId") String processId);


    /**
     * retrieve the status of a job
     *
     * @param jobId     The id of a job (required)
     * @param processId The id of the process (required)
     * @return Call&lt;StatusInfo&gt;
     */
    @GET("processes/{processId}/jobs/{jobId}")
    @Headers({"Accept: application/json"})
    Call<StatusInfo> getStatus(@Path("processId") String processId, @Path("jobId") String jobId);

    /**
     * retrieve the result(s) of a job
     *
     * @param jobId     The id of a job (required)
     * @param processId The id of the process (required)
     * @return Call&lt;Result&gt;
     */
    @GET("processes/{processId}/jobs/{jobId}/result")
    @Headers({"Accept: application/json"})
    Call<Result> getResult(@Path("processId") String processId, @Path("jobId") String jobId);

}
