package org.n52.testbed.routing.api.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import okhttp3.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import retrofit2.Response;

import java.io.IOException;

public class HttpStatusError extends RuntimeException {

    @JsonIgnore
    private final HttpStatus status;
    private final Object body;
    private final MediaType mediaType;


    public HttpStatusError(HttpStatus status, Throwable cause) {
        this.status = status;
        this.body = cause;
        this.mediaType = MediaType.APPLICATION_JSON;
    }

    public HttpStatusError(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.body = this;
        this.mediaType = MediaType.APPLICATION_JSON;
    }

    public HttpStatusError(HttpStatus status) {
        this.status = status;
        this.body = this;
        this.mediaType = MediaType.APPLICATION_JSON;
    }

    public HttpStatusError(Response<?> response) {
        byte[] body = null;
        MediaType mediaType = null;
        this.status = HttpStatus.valueOf(response.code());
        ResponseBody responseBody = response.errorBody();
        if (responseBody != null) {
            okhttp3.MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                mediaType = MediaType.valueOf(contentType.toString());
                try {
                    body = responseBody.bytes();
                } catch (IOException ignored) {
                }
            }
        }
        this.body = body != null ? body : this;
        this.mediaType = body != null ? mediaType : MediaType.APPLICATION_JSON;
    }

    public static <T> Response<T> throwIfNotSuccessful(Response<T> response) {
        if (response.isSuccessful()) {
            return response;
        }
        throw new HttpStatusError(response);
    }

    public Object getBody() {
        return body;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
