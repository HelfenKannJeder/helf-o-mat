package de.helfenkannjeder.helfomat.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Valentin Zickner
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error("Handle IllegalArgumentException", e);
        return createErrorResponse(HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity<ErrorDetails> createErrorResponse(HttpStatus status, Exception e) {
        ErrorDetails error = new ErrorDetails(e.getClass().getSimpleName());
        return new ResponseEntity<>(error, new HttpHeaders(), status);
    }

    private static class ErrorDetails {
        private final String exceptionName;

        ErrorDetails(String exceptionName) {
            this.exceptionName = exceptionName;
        }

        public String getExceptionName() {
            return exceptionName;
        }
    }

}
