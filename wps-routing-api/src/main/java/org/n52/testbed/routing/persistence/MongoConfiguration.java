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
package org.n52.testbed.routing.persistence;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import javax.annotation.PreDestroy;
import java.util.List;


@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration {
    private final MongoClientOptions options;
    private final MongoProperties properties;
    private final MongoClientFactory factory;
    private MongoClient mongo;
    private List<Converter<?, ?>> converters;

    public MongoConfiguration(MongoProperties properties,
                              ObjectProvider<MongoClientOptions> options,
                              Environment environment) {
        this.options = options.getIfAvailable();
        this.properties = properties;
        this.factory = new MongoClientFactory(properties, environment);
    }

    @PreDestroy
    public void close() {
        if (this.mongo != null) {
            this.mongo.close();
        }
    }

    @Override
    protected String getDatabaseName() {
        if (properties.getUri() != null) {
            MongoClientOptions.Builder builder = options != null
                    ? MongoClientOptions.builder(options) : MongoClientOptions.builder();
            return new MongoClientURI(properties.getUri(), builder).getDatabase();
        }
        return properties.getDatabase();
    }

    @Override
    public CustomConversions customConversions() {
        return new MongoCustomConversions(this.converters);
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        this.mongo = this.factory.createMongoClient(this.options);
        return this.mongo;
    }

    @Autowired
    @MongoConverter
    public void setConverters(List<Converter<?, ?>> converters) {
        this.converters = converters;
    }
}
