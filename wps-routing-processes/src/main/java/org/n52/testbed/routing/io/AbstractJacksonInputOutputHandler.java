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
package org.n52.testbed.routing.io;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.n52.javaps.description.TypedProcessInputDescription;
import org.n52.javaps.description.TypedProcessOutputDescription;
import org.n52.javaps.io.*;
import org.n52.shetland.ogc.wps.Format;
import org.n52.testbed.routing.model.MediaTypes;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Set;

public abstract class AbstractJacksonInputOutputHandler extends AbstractInputOutputHandler
        implements InputHandler, OutputHandler {

    protected static final Format JSON_FORMAT = new Format(MediaTypes.APPLICATION_JSON);

    private final ObjectMapper objectMapper;

    public AbstractJacksonInputOutputHandler() {
        this.objectMapper = configureObjectMapper();
    }

    private ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    protected ObjectMapper configureObjectMapper() {
        return new ObjectMapper();
    }

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.singleton(JSON_FORMAT);
    }

    @Override
    public InputStream generate(TypedProcessOutputDescription<?> description, Data<?> data, Format format)
            throws IOException, EncodingException {
        return writeValue(checkBindingType(data).getPayload(), format);
    }

    @Override
    public void addSupportedBinding(Class<? extends Data<?>> bindingType) {
        Constructor<?>[] constructors = bindingType.getConstructors();
        if (constructors.length != 1 || constructors[0].getParameterCount() != 1) {
            throw new IllegalArgumentException("bindings require a single argument constructor");
        }
        super.addSupportedBinding(bindingType);
    }

    @Override
    public Data<?> parse(TypedProcessInputDescription<?> description, InputStream input, Format format)
            throws IOException, DecodingException {
        Object payload = readValue(input, format, description.getPayloadType());
        Class<? extends Data<?>> bindingType = checkBindingType(description.getBindingType());
        return createBinding(bindingType, payload);
    }

    private Data<?> createBinding(Class<? extends Data<?>> bindingType, Object payload) throws DecodingException {
        try {
            Constructor<?>[] constructors = bindingType.getConstructors();
            if (constructors.length != 1 || constructors[0].getParameterCount() != 1
                    || !constructors[0].getParameterTypes()[0].isAssignableFrom(payload.getClass())) {
                throw new DecodingException("bindings require a single argument constructor");
            }
            return (Data<?>) constructors[0].newInstance(payload);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DecodingException("can not instantiate binding", e);
        }
    }

    private <T> T readValue(InputStream input, Format format, Class<? extends T> type)
            throws DecodingException, IOException {
        try (InputStreamReader reader = new InputStreamReader(input, format.getEncodingAsCharsetOrDefault())) {
            return getObjectMapper().readValue(reader, type);
        } catch (JsonParseException | JsonMappingException e) {
            throw new DecodingException("can not decode type " + type, e);
        }
    }

    private InputStream writeValue(Object value, Format format) throws EncodingException, IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, format.getEncodingAsCharsetOrDefault())) {
            getObjectMapper().writeValue(writer, value);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (JsonProcessingException e) {
            throw new EncodingException("can not encode value" + value.getClass(), e);
        }
    }

    private Data<?> checkBindingType(Data<?> data) throws EncodingException {
        Class<? extends Data> bindingType = data.getClass();
        if (getSupportedBindings().stream().noneMatch(x -> x.isAssignableFrom(bindingType))) {
            throw new EncodingException("unsupported binding type: " + bindingType);
        }
        return data;
    }

    private <T> Class<T> checkBindingType(Class<T> bindingType) throws DecodingException {
        if (getSupportedBindings().stream().noneMatch(x -> x.isAssignableFrom(bindingType))) {
            throw new DecodingException("unsupported binding type: " + bindingType);
        }
        return bindingType;
    }
}
