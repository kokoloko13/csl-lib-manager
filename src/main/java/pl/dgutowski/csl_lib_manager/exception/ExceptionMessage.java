package pl.dgutowski.csl_lib_manager.exception;

public final class ExceptionMessage {
    private ExceptionMessage(){}

    public static String EMAIL_ADDRESS_IN_USE = "E-mail address is already in use";
    public static String EMAIL_OR_PASSWORD_INVALID = "Invalid e-mail address or password";
    public static String REQUEST_VALIDATION = "Request is not valid";
    public static String RESOURCE_NOT_FOUND = "%s with id %s not found";
    public static String INVALID_TOKEN = "Token is invalid";

}
