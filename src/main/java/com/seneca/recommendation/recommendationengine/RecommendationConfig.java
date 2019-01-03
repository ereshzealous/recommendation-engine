package com.seneca.recommendation.recommendationengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seneca.recommendation.recommendationengine.rest.CustomObjectMapper;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.List;

/**
 * @author Gorantla, Eresh
 * @created 03-01-2019
 */
@Configuration
public class RecommendationConfig implements ServletContextInitializer, WebMvcConfigurer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

    }

    @Bean(name = "jsonMapper")
    @Primary
    public ObjectMapper jsonMapper() {
        return new CustomObjectMapper();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(jsonMapper()));
    }
}
