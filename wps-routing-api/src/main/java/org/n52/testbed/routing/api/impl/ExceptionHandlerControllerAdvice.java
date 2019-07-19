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
