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
package org.n52.testbed.routing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.n52.testbed.routing.model.MediaTypes;
import org.n52.testbed.routing.model.routing.Route;
import org.n52.testbed.routing.model.routing.RouteInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

@Service
public class NotifyService {
    private static final Logger LOG = LoggerFactory.getLogger(NotifyService.class);
    @Autowired
    private OkHttpClient httpClient;
    @Autowired
    private ObjectMapper objectMapper;

    public void notifySubscriber(RouteInfo routeInfo, URI subscriber, Route route) {
        try {

            httpClient.newCall(createRequest(route, subscriber)).enqueue(new NotifyCallback(routeInfo));
        } catch (JsonProcessingException e) {
            failure(routeInfo, e);
        }
    }

    private Request createRequest(Route route, URI subscriber) throws JsonProcessingException {
        RequestBody body = RequestBody.create(MediaType.get(MediaTypes.APPLICATION_JSON),
                                              objectMapper.writeValueAsBytes(route));

        return new Request.Builder().url(subscriber.toString()).post(body).build();
    }

    private static void failure(RouteInfo routeInfo, Throwable throwable) {
        LOG.warn(String.format("Failed to notify subscriber for %s", routeInfo.getIdentifier()), throwable);
    }

    private static class NotifyCallback implements Callback {
        private final RouteInfo routeInfo;

        NotifyCallback(RouteInfo routeInfo) {
            this.routeInfo = routeInfo;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            failure(routeInfo, e);
        }

        @Override
        public void onResponse(Call call, Response response) {
            if (response.isSuccessful()) {
                LOG.info("Successfully notified subscriber for {}",
                         routeInfo.getIdentifier());
            } else {
                LOG.warn("Subscriber for {} returned failure: {} {}",
                         routeInfo.getIdentifier(), response.code(), response.message());
            }

        }
    }
}
