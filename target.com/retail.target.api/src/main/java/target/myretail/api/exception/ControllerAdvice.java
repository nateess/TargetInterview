package target.myretail.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import target.myretail.api.model.Error;

@Slf4j
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(WebClientResponseException.class)
    ResponseEntity<Error> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("ControllerAdvice, method=handleWebClientResponseException");
        return ResponseEntity.status(ex.getRawStatusCode()).body(new Error(ex.getStatusCode().value(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Error> handleException(Exception ex) {
        log.error("ControllerAdvice, method=handleException");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<Error> handleRunTimeException(Exception ex) {
        log.error("ControllerAdvice, method=handleRunTimeException");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }
}
