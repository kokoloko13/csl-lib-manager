package pl.dgutowski.csl_lib_manager.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ApiError(
        String path,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) Map<String, String> validationErrors,
        int statusCode,
        LocalDateTime localDateTime
) {
    @Override
    public String toString() {
        return """
                {
                    "path": "%s",
                    "message": "%s",
                    "statusCode": %d,
                    "localDateTime": "%s"
                }
                """.formatted(path, message, statusCode, localDateTime);
    }
}
