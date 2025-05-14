package pl.dgutowski.csl_lib_manager.book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    private String title;
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    private String author;
    @Size(max = 4, message = "Year of publication cannot be longer than 4 digits")
    private String pubYear;

}
