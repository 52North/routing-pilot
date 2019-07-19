package org.n52.testbed.routing.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.n52.jackson.datatype.jts.JtsModule;
import org.n52.javaps.io.OutputHandler;
import org.n52.shetland.ogc.wps.Format;
import org.n52.testbed.routing.model.MediaTypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RouteOutputHandler extends AbstractJacksonInputOutputHandler implements OutputHandler {

    private static final Format GEO_JSON_FORMAT = new Format(MediaTypes.APPLICATION_GEO_JSON);
    private static final HashSet<Format> SUPPORTED_FORMATS = new HashSet<>(Arrays.asList(GEO_JSON_FORMAT, JSON_FORMAT));

    public RouteOutputHandler() {
        addSupportedBinding(RouteData.class);
    }

    @Override
    protected ObjectMapper configureObjectMapper() {
        return super.configureObjectMapper().registerModule(new JtsModule());
    }

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(SUPPORTED_FORMATS);
    }
}
