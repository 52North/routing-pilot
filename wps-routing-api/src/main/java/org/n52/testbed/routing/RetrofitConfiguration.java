package org.n52.testbed.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.n52.testbed.routing.client.OptionRoutesApi;
import org.n52.testbed.routing.client.OgcProcessingApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.URI;

@Configuration
public class RetrofitConfiguration {
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
        Converter.Factory converterFactory = JacksonConverterFactory.create(objectMapper);
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
