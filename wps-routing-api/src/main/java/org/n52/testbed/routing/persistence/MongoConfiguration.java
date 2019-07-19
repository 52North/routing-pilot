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
