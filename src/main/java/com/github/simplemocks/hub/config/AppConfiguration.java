package com.github.simplemocks.hub.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.simple_mocks.async.embedded.EnableAsyncServiceEmbedded;
import com.github.simple_mocks.content.embedded.EnableContentServiceEmbedded;
import com.github.simple_mocks.error.embedded.EnableErrorServiceEmbedded;
import com.github.simple_mocks.localization.embedded.EnableLocalizationServiceEmbedded;
import com.github.simple_mocks.session.embedded.EnableSessionServiceEmbedded;
import com.github.simple_mocks.storage.embedded.EnableStorageServiceEmbedded;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Configuration
@EnableContentServiceEmbedded
@EnableErrorServiceEmbedded
@EnableLocalizationServiceEmbedded
@EnableSessionServiceEmbedded
@EnableStorageServiceEmbedded
@EnableAsyncServiceEmbedded
public class AppConfiguration {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();
    }
}
