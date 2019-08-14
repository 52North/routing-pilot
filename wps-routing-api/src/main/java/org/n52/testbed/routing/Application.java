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
package org.n52.testbed.routing;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.n52.jackson.datatype.jts.IncludeBoundingBox;
import org.n52.jackson.datatype.jts.JtsModule;
import org.n52.testbed.routing.client.OgcProcessingApi;
import org.n52.testbed.routing.client.OptionRoutesApi;
import org.n52.testbed.routing.model.routing.Route;
import org.n52.testbed.routing.model.routing.RouteDefinition;
import org.n52.testbed.routing.persistence.MongoJacksonConverterFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Main entry point.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMongoRepositories(basePackages = {"org.n52.testbed.routing.persistence"})
@SuppressWarnings("UncommentedMain")
public class Application {
    /*
     * Start the application.
     *
     * @param args The arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Configuration
    public static class JacksonConfiguration {

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

    @Configuration
    public class MongoConfiguration {

        @Bean
        public MongoCustomConversions customConversions(MongoJacksonConverterFactory factory) {
            List<?> converters = Arrays.asList(
                    factory.forType(Route.class),
                    factory.forType(RouteDefinition.class),
                    factory.forType(Geometry.class),
                    factory.forType(Point.class),
                    factory.forType(LineString.class),
                    factory.forType(Polygon.class),
                    factory.forType(MultiPoint.class),
                    factory.forType(MultiLineString.class),
                    factory.forType(MultiPolygon.class),
                    factory.forType(GeometryCollection.class));
            return new MongoCustomConversions(converters);
        }

    }

    @Configuration
    public static class RetrofitConfiguration {
        private URI processingEndpoint;

        @Bean
        public OgcProcessingApi optionWpsApi(@Qualifier("processing-api") Retrofit retrofit) {
            return retrofit.create(OgcProcessingApi.class);
        }

        @Bean
        public OptionRoutesApi optionRoutesApi(@Qualifier("processing-api") Retrofit retrofit) {
            return retrofit.create(OptionRoutesApi.class);
        }

        @Bean
        @Qualifier("processing-api")
        public Retrofit retrofit(ObjectMapper objectMapper, OkHttpClient client) {
            JacksonConverterFactory converterFactory = JacksonConverterFactory.create(objectMapper);
            String baseUrl = this.processingEndpoint.toASCIIString();

            return new Retrofit.Builder().client(client)
                                         .addConverterFactory(converterFactory).baseUrl(baseUrl).build();
        }

        @Bean
        public OkHttpClient okHttpClient() {
            return new OkHttpClient.Builder().followRedirects(true).build();
        }

        @Value("${routing.endpoint}")
        public void setProcessingEndpoint(URI processingEndpoint) {
            this.processingEndpoint = processingEndpoint;
        }
    }

    @Configuration
    public static class WebConfiguration implements WebMvcConfigurer {
        @Bean
        @Scope("request")
        public UriComponentsBuilder uriComponentsBuilder(NativeWebRequest webRequest) {
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            if (request == null) {
                return null;
            }
            return ServletUriComponentsBuilder.fromServletMapping(request);
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedHeaders("*")
                    .allowedMethods("*")
                    .maxAge(3600)
                    .allowCredentials(true);
        }
    }
}
