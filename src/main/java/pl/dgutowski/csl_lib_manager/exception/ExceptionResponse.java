package pl.dgutowski.csl_lib_manager.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String details;

    public ExceptionResponse(HttpStatus httpStatus, String errorCode, String details) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.details = details;
    }

    public ExceptionResponse(HttpStatus httpStatus, String errorCode) {
        this(httpStatus, errorCode, "");
    }


}
