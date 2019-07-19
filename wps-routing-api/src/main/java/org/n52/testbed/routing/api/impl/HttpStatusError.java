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

import com.fasterxml.jackson.annotation.JsonIgnore;
import okhttp3.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import retrofit2.Response;

import java.io.IOException;

public class HttpStatusError extends RuntimeException {
    private static final long serialVersionUID = -7158896170454426289L;
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
        byte[] bytes = null;
        MediaType mt = null;
        this.status = HttpStatus.valueOf(response.code());
        ResponseBody responseBody = response.errorBody();
        if (responseBody != null) {
            okhttp3.MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                mt = MediaType.valueOf(contentType.toString());
                try {
                    bytes = responseBody.bytes();
                } catch (IOException ignored) {
                }
            }
        }
        this.body = bytes != null ? bytes : this;
        this.mediaType = bytes != null ? mt : MediaType.APPLICATION_JSON;
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
