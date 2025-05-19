package pl.dgutowski.csl_lib_manager.exception;

public class EmailAddressInUseException extends RuntimeException {
    public EmailAddressInUseException(String message) {
        super(message);
    }
}
