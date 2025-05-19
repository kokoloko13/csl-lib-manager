package pl.dgutowski.csl_lib_manager.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class BookDTO {
    private UUID id;
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    private String title;
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    private String author;
    @Size(max = 4, message = "Year of publication cannot be longer than 4 digits")
    private String pubYear;
}
