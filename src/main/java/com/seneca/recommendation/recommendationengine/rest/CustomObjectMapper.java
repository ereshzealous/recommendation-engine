package com.seneca.recommendation.recommendationengine.rest;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author Gorantla, Eresh
 * @created 03-01-2019
 */
public class CustomObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 3661389953847784217L;

    public CustomObjectMapper() {
        super();
        setSerializationInclusion(Include.NON_NULL);
        setSerializationInclusion(Include.NON_EMPTY);
        configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        registerModule(new JavaTimeModule());
    }
}
