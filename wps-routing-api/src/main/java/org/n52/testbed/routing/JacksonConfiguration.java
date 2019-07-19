package org.n52.testbed.routing;

import com.fasterxml.jackson.databind.Module;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.n52.jackson.datatype.jts.IncludeBoundingBox;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public Module jtsModule(GeometryFactory geometryFactory) {
        return new JtsModule(geometryFactory, IncludeBoundingBox.exceptPoints());
    }

    @Bean
    public GeometryFactory geometryFactory(PrecisionModel precisionModel) {
        return new GeometryFactory(precisionModel, 4326);
    }

    @Bean
    public PrecisionModel precisionModel() {
        return new PrecisionModel(PrecisionModel.FLOATING);
    }
}
