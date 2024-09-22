package com.github.simplemocks.hub.config;

import com.github.simplemocks.hub.logger.ApiLoggerFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Configuration
@EnableWebMvc
public class HubWebConf implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/web/app/hub/static/")
                .resourceChain(true)
                .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/")
                .setViewName("forward:/index.html");
        registry.addViewController("/apps/**")
                .setViewName("forward:/index.html");
    }

    @Bean
    public FilterRegistrationBean<ApiLoggerFilter> apiLoggingFilter() {
        var registrationBean = new FilterRegistrationBean<ApiLoggerFilter>();

        registrationBean.setFilter(new ApiLoggerFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }
}
