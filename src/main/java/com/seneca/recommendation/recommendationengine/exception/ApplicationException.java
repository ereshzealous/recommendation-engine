package com.seneca.recommendation.recommendationengine.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Gorantla, Eresh
 * @created 03-01-2019
 */
@Getter
@Setter
public class ApplicationException extends Exception {
    private String fieldName;
    private String errorKey;
    private String errorMessage;

    private final String DEFAULT_ERROR_KEY = "error.unexpected";
    private final String DEFAULT_ERROR_MSG = "Unexpected error occurred";

    public ApplicationException(String fieldName, String errorKey, String errorMessage) {
        this.fieldName = fieldName;
        this.errorKey = errorKey;
        this.errorMessage = errorMessage;
    }

    public ApplicationException(String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorKey = DEFAULT_ERROR_KEY;
    }
}
