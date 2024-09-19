package com.github.simplemocks.hub.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.simplemocks.async.embedded.EnableAsyncServiceEmbedded;
import com.github.simplemocks.content.embedded.EnableContentServiceEmbedded;
import com.github.simplemocks.error_service.embedded.EnableErrorServiceEmbedded;
import com.github.simplemocks.localization.embedded.EnableLocalizationServiceEmbedded;
import com.github.simplemocks.session.embedded.EnableSessionServiceEmbedded;
import com.github.simplemocks.storage.embedded.EnableStorageServiceEmbedded;
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
