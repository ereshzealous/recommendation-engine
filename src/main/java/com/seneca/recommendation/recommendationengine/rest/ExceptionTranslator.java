package com.seneca.recommendation.recommendationengine.rest;

import com.seneca.recommendation.recommendationengine.exception.ApplicationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Gorantla, Eresh
 * @created 03-01-2019
 */
@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<Object> handleException(Throwable ex, WebRequest request) {
        ApplicationException applicationException = null;
        Throwable rootCause = ex.getCause();

        if (rootCause instanceof ApplicationException) {
            applicationException = (ApplicationException) rootCause;
        }

        if (applicationException == null) {
            applicationException = new ApplicationException(ex.getMessage());
        }

        int status = 500;
        RestFault restFault = new RestFault();
        restFault.setErrorKey(applicationException.getErrorKey());
        restFault.setErrorMessage(applicationException.getErrorMessage());
        restFault.setFieldName(applicationException.getFieldName());
        restFault.setStatus("Application Error");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<Object>(restFault, headers, HttpStatus.valueOf(status));
    }

    public class RestFault {
        private String status;
        private String fieldName;
        private String errorKey;
        private String errorMessage;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getErrorKey() {
            return errorKey;
        }

        public void setErrorKey(String errorKey) {
            this.errorKey = errorKey;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}


