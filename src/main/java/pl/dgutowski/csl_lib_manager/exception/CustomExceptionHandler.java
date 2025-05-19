package pl.dgutowski.csl_lib_manager.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NoHandlerFoundException e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(getApiError(e, httpStatus, request), httpStatus);
    }

    @ExceptionHandler(EmailAddressInUseException.class)
    public ResponseEntity<ApiError> handleException(EmailAddressInUseException e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return new ResponseEntity<>(getApiError(e, httpStatus, request), httpStatus);
    }

    @ExceptionHandler({
            EmailOrPasswordInvalidException.class,
            InvalidTokenException.class
    })
    public ResponseEntity<ApiError> handleUnauthorizedException(Exception e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(getApiError(e, httpStatus, request), httpStatus);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleException(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(getApiError(e, httpStatus, request), httpStatus);
    }

    @ExceptionHandler({
            RequestValidationException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ApiError> handleValidationException(Exception e, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(getApiError(e, httpStatus, request, true), httpStatus);
    }

    private ApiError getApiError(Exception e, HttpStatus httpStatus, HttpServletRequest request) {
        return getApiError(e, httpStatus, request, false);
    }

    private ApiError getApiError(Exception e, HttpStatus httpStatus, HttpServletRequest request, boolean validationError) {
        var apiError = ApiError.builder()
                .path(request.getRequestURI())
                .message(e.getMessage())
                .statusCode(httpStatus.value())
                .localDateTime(LocalDateTime.now());

        if(validationError) {
            Map<String, String> validationErrors = new HashMap<>();

            if(e instanceof MethodArgumentNotValidException exception) {
                validationErrors = exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            }

            if(e instanceof MethodArgumentTypeMismatchException exception) {
                validationErrors.put(exception.getPropertyName(), exception.getMessage());
            }

            if(e instanceof ConstraintViolationException exception) {
                validationErrors = exception.getConstraintViolations()
                        .stream()
                        .collect(Collectors.toMap(
                                violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage
                        ));
            }

            apiError.validationErrors(validationErrors);
        }

        return apiError.build();
    }
}
