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

import org.n52.testbed.routing.service.RouteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handle(Throwable throwable) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(throwable);
    }

    @ExceptionHandler(RouteNotFoundException.class)
    public ResponseEntity<Object> handle(RouteNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }

    @ExceptionHandler(HttpStatusError.class)
    public ResponseEntity<Object> handle(HttpStatusError error) {
        Object body = error.getBody() == null ? error : error.getBody();
        HttpStatus status = error.getStatus() == null ? HttpStatus.INTERNAL_SERVER_ERROR : error.getStatus();
        MediaType contentType = error.getMediaType() == null ? MediaType.APPLICATION_JSON : error.getMediaType();
        return ResponseEntity.status(status).contentType(contentType).body(body);
    }

}
