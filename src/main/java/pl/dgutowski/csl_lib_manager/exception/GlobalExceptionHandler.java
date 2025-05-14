package pl.dgutowski.csl_lib_manager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExceptionResponse.class)
    public ResponseEntity<Map<String, Object>> handleException(ExceptionResponse exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", exception.getErrorCode());
        response.put("message", exception.getMessage());
        response.put("details", exception.getDetails());

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(response);
    }

}
