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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.testbed.routing.service.RouteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Optional;
import java.util.Set;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    private static final String STACK_TRACE = "stackTrace";
    private static final String MESSAGE = "message";
    private static final String REASON_PHRASE = "reasonPhrase";
    private static final String STATUS_CODE = "statusCode";
    private static final String SUPPRESSED = "suppressed";
    private static final String CAUSED_BY = "causedBy";
    private static final String TYPE = "type";
    private static final String CIRCULAR_REFERENCE = "circularReference";

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ObjectNode handle(Throwable throwable) {
        return encodeJSON(HttpStatus.INTERNAL_SERVER_ERROR, throwable);
    }

    @ExceptionHandler(RouteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ObjectNode handle(RouteNotFoundException exception) {
        return encodeJSON(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ObjectNode> handle(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatus()).body(encodeJSON(exception.getStatus(), exception));
    }

    @ExceptionHandler(HttpStatusError.class)
    public ResponseEntity<Object> handle(HttpStatusError error) {
        HttpStatus status = error.getStatus() == null ? HttpStatus.INTERNAL_SERVER_ERROR : error.getStatus();
        MediaType contentType = error.getMediaType() == null ? MediaType.APPLICATION_JSON : error.getMediaType();

        Object body;
        if (error.getBody() == null) {
            if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                body = encodeJSON(status, error);
            } else {
                body = error;
            }
        } else {
            body = error.getBody();
        }
        return ResponseEntity.status(status).contentType(contentType).body(body);
    }

    private ObjectNode encodeJSON(HttpStatus status, Throwable throwable) {
        ObjectNode node = getJsonFactory().objectNode();
        node.put(STATUS_CODE, status.value());
        node.put(REASON_PHRASE, status.getReasonPhrase());
        if (throwable.getMessage() != null) {
            node.put(MESSAGE, throwable.getMessage());
        }
        node.set(STACK_TRACE, encodeStackTrace(throwable));
        return node;
    }

    private ObjectNode encodeStackTrace(Throwable t) {
        Set<Throwable> dejavu = Collections.newSetFromMap(new IdentityHashMap<>());
        dejavu.add(t);
        ObjectNode throwableNode = getJsonFactory().objectNode();

        throwableNode.put(TYPE, t.getClass().getName());
        if (t.getMessage() != null) {
            throwableNode.put(MESSAGE, t.getMessage());
        }

        StackTraceElement[] trace = t.getStackTrace();
        if (trace.length > 0) {
            ArrayNode at = throwableNode.putArray("at");
            Arrays.stream(trace)
                  .map(this::encodeStackTraceElement)
                  .forEach(at::add);
        }

        return addCauseAndSuppressed(t, dejavu, throwableNode, trace);
    }

    private ObjectNode encodeEnclosedStackTrace(Throwable t, StackTraceElement[] enclosingTrace,
                                                Set<Throwable> dejavu) {
        ObjectNode throwableNode = getJsonFactory().objectNode();
        throwableNode.put(TYPE, t.getClass().getName());
        if (t.getMessage() != null) {
            throwableNode.put(MESSAGE, t.getMessage());
        }

        if (dejavu.contains(t)) {
            ObjectNode circularNode = getJsonFactory().objectNode();
            circularNode.set(CIRCULAR_REFERENCE, throwableNode);
            return circularNode;
        }
        dejavu.add(t);

        StackTraceElement[] trace = t.getStackTrace();
        int m = trace.length - 1;
        int n = enclosingTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
            m--;
            n--;
        }
        int common = trace.length - 1 - m;
        ArrayNode at = throwableNode.putArray("at");
        Arrays.stream(trace, 0, m + 1).map(this::encodeStackTraceElement).forEach(at::add);
        if (common > 0) {
            at.add(String.format("... %d more", common));
        }

        return addCauseAndSuppressed(t, dejavu, throwableNode, trace);
    }

    private ObjectNode addCauseAndSuppressed(Throwable t, Set<Throwable> dejavu, ObjectNode throwableNode,
                                             StackTraceElement[] trace) {
        Throwable[] suppressed = t.getSuppressed();
        if (suppressed.length > 0) {
            ArrayNode suppressedNode = throwableNode.putArray(SUPPRESSED);
            Arrays.stream(suppressed).map(se -> encodeEnclosedStackTrace(se, trace, dejavu))
                  .forEach(suppressedNode::add);
        }
        Optional.ofNullable(t.getCause())
                .map(cause -> encodeEnclosedStackTrace(cause, trace, dejavu))
                .ifPresent(cause -> throwableNode.set(CAUSED_BY, cause));
        return throwableNode;
    }

    private JsonNodeCreator getJsonFactory() {
        return JsonNodeFactory.instance;
    }

    private JsonNode encodeStackTraceElement(StackTraceElement element) {
        return getJsonFactory().textNode(element.toString());
    }
}
