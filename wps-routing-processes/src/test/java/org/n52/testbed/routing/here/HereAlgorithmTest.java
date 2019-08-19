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
package org.n52.testbed.routing.here;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import junit.framework.AssertionFailedError;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.n52.javaps.engine.Engine;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.impl.StringValueProcessData;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.testbed.routing.model.MediaTypes;
import org.n52.testbed.routing.model.routing.Route;
import org.n52.testbed.routing.model.routing.RouteFeature;
import org.n52.testbed.routing.model.routing.RouteFeatureProperties;
import org.n52.testbed.routing.model.routing.RouteFeatureType;
import org.n52.testbed.routing.model.wps.Inputs;
import org.n52.testbed.routing.model.wps.Outputs;
import org.n52.testbed.routing.model.wps.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)

@ContextConfiguration(value = "classpath:/test.xml", loader = ProviderAwareGenericXmlContextLoader.class)
public class HereAlgorithmTest {
    private static final Format GEO_JSON_FORMAT = new Format(MediaTypes.APPLICATION_GEO_JSON);
    private static final Format TEXT_PLAIN_FORMAT = new Format(MediaTypes.TEXT_PLAIN);
    private static final OwsCode PROCESS_IDENTIFIER = new OwsCode("org.n52.routing.here");
    @Autowired
    private Engine engine;
    @Autowired
    private ObjectMapper objectMapper;
    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @BeforeClass
    public static void setSystemProperties() {
        Dotenv dotenv = Dotenv.configure().directory("..").load();
        System.setProperty(HereAlgorithm.HERE_APP_CODE_PROPERTY, dotenv.get(HereAlgorithm.HERE_APP_CODE_ENV_VARIABLE, ""));
        System.setProperty(HereAlgorithm.HERE_APP_ID_PROPERTY, dotenv.get(HereAlgorithm.HERE_APP_ID_ENV_VARIABLE, ""));
    }

    @Before
    public void setUp() {
        Objects.requireNonNull(engine);
    }

    @Test
    public void checkDescription() {
        ProcessDescription description = engine.getProcessDescription(PROCESS_IDENTIFIER)
                .orElseThrow(() -> new AssertionFailedError("missing process in engine"));
        errors.checkThat(description.getVersion(), is("1.0.0"));
        errors.checkThat(description.getId(), is(PROCESS_IDENTIFIER));
        errors.checkThat(description.getOutputDescriptions(), hasSize(1));
        errors.checkThat(description.getOutputDescriptions().iterator().next().getId(), is(new OwsCode(Outputs.ROUTE)));
        errors.checkThat(description.getInputDescriptions(), hasSize(9));
        errors.checkThat(description.getInputs().stream().map(OwsCode::getValue).collect(toList()),
                         hasItems(Inputs.NAME, Inputs.ORIGIN, Inputs.DESTINATION, Inputs.PREFERENCE, Inputs.WHEN,
                                  Inputs.INTERMEDIATES, Inputs.OBSTACLES, Inputs.MAX_HEIGHT, Inputs.MAX_WEIGHT));

    }

    @Test
    public void checkExecution() throws Exception {

        LinkedList<ProcessData> inputs = new LinkedList<>();

        inputs.add(new StringValueProcessData(new OwsCode(Inputs.NAME), TEXT_PLAIN_FORMAT, "the-name-of-the-route"));
        inputs.add(new StringValueProcessData(new OwsCode(Inputs.ORIGIN), GEO_JSON_FORMAT, "{\"type\": \"Point\", \"coordinates\": [-77.047712, 38.892346]}"));
        inputs.add(new StringValueProcessData(new OwsCode(Inputs.DESTINATION), GEO_JSON_FORMAT, "{\"type\": \"Point\", \"coordinates\": [-76.994730, 38.902629]}"));
        inputs.add(new StringValueProcessData(new OwsCode(Inputs.PREFERENCE), TEXT_PLAIN_FORMAT, "fastest"));

        OutputDefinition outputDefinition = new OutputDefinition();
        outputDefinition.setId(new OwsCode(Outputs.ROUTE));
        outputDefinition.setFormat(GEO_JSON_FORMAT);
        outputDefinition.setDataTransmissionMode(DataTransmissionMode.VALUE);

        JobId jobId = engine.execute(PROCESS_IDENTIFIER, inputs, Collections.singletonList(outputDefinition), ResponseMode.RAW);

        Result result = engine.getResult(jobId).get();

        errors.checkThat(result.getOutputs(), hasSize(1));
        errors.checkThat(result.getOutputs().get(0).getId().getValue(), is(Outputs.ROUTE));
        errors.checkThat(result.getOutputs().get(0).isValue(), is(true));
        errors.checkThat(result.getOutputs().get(0).asValue().getFormat(), is(GEO_JSON_FORMAT));
        InputStream data = result.getOutputs().get(0).asValue().getData();

        Route route = objectMapper.readValue(data, Route.class);

        errors.checkThat(route.getName(), is("the-name-of-the-route"));
        errors.checkThat(route.getStatus(), is(Status.SUCCESSFUL));
        List<RouteFeature> features = route.getFeatures();
        errors.checkThat(features, hasSize(12));

        errors.checkThat(features.get(0).getGeometry(), is(Matchers.instanceOf(LineString.class)));
        errors.checkThat(features.get(0).getProperties().get(RouteFeatureProperties.TYPE), is(RouteFeatureType.OVERVIEW.toString()));

        errors.checkThat(features.get(1).getGeometry(), is(Matchers.instanceOf(Point.class)));
        errors.checkThat(features.get(1).getProperties().get(RouteFeatureProperties.TYPE), is(RouteFeatureType.START.toString()));
        for (int i = 2; i < 11; i++) {
            errors.checkThat(features.get(i).getGeometry(), is(Matchers.instanceOf(LineString.class)));
            errors.checkThat(features.get(i).getProperties().get(RouteFeatureProperties.TYPE), is(RouteFeatureType.SEGMENT.toString()));
        }
        errors.checkThat(features.get(11).getProperties().get(RouteFeatureProperties.TYPE), is(RouteFeatureType.END.toString()));
        errors.checkThat(features.get(11).getGeometry(), is(Matchers.instanceOf(Point.class)));
    }

}
