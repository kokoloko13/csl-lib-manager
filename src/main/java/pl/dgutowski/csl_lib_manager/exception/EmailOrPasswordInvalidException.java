package pl.dgutowski.csl_lib_manager.exception;

public class EmailOrPasswordInvalidException extends RuntimeException {
    public EmailOrPasswordInvalidException(String message) {
        super(message);
    }
}
