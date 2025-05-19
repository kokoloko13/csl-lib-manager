package pl.dgutowski.csl_lib_manager.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.dgutowski.csl_lib_manager.AbstractTestcontainers;
import pl.dgutowski.csl_lib_manager.auth.JwtService;
import pl.dgutowski.csl_lib_manager.book.dto.BookDTO;
import pl.dgutowski.csl_lib_manager.user.User;
import pl.dgutowski.csl_lib_manager.user.UserRepository;
import pl.dgutowski.csl_lib_manager.user.UserRole;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class BookIntTest extends AbstractTestcontainers {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    private String TEST_TOKEN;

    @BeforeEach
    void cleanUp() {
        bookRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.save(
                User.builder()
                        .name("Test")
                        .email("test@test.pl")
                        .password(encoder.encode("Test123!"))
                        .role(UserRole.USER)
                        .build()
        );

        TEST_TOKEN = jwtService.generateToken("test@test.pl", UserRole.USER);
    }


    @Test
    void addBook_Forbidden() throws Exception {
        BookDTO newBookBody = BookDTO.builder()
                .title("Int Test")
                .author("Int Test")
                .pubYear("0000")
                .build();

        mockMvc.perform(
                post("/api/v1/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookBody))
        ).andExpect(status().isForbidden());
    }

    @Test
    void addBook() throws Exception {
        BookDTO newBookBody = BookDTO.builder()
                .title("Int Test")
                .author("Int Test")
                .pubYear("0000")
                .build();

        mockMvc.perform(
                post("/api/v1/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
                        .content(objectMapper.writeValueAsString(newBookBody))
        ).andExpect(status().isCreated());
    }

    @Test
    void getBook() throws Exception {
        BookDTO newBookBody = BookDTO.builder()
                .title("Int Test")
                .author("Int Test")
                .pubYear("0000")
                .build();

        MvcResult result = mockMvc.perform(
                post("/api/v1/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
                        .content(objectMapper.writeValueAsString(newBookBody))
        ).andExpect(status().isCreated()).andReturn();

        String json = result.getResponse().getContentAsString();
        String bookId = objectMapper.readValue(json, BookDTO.class).getId().toString();

        mockMvc.perform(
                get("/api/v1/book/%s".formatted(bookId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
                        .content(objectMapper.writeValueAsString(newBookBody))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isString())

                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.title").isString())
                .andExpect(jsonPath("$.title").value(newBookBody.getTitle()))

                .andExpect(jsonPath("$.author").exists())
                .andExpect(jsonPath("$.author").isString())
                .andExpect(jsonPath("$.author").value(newBookBody.getAuthor()))

                .andExpect(jsonPath("$.pubYear").exists())
                .andExpect(jsonPath("$.pubYear").isString())
                .andExpect(jsonPath("$.pubYear").value(newBookBody.getPubYear()));
    }

    @Test
    void getBook_NotExists() throws Exception {

        UUID testUuid = UUID.randomUUID();

        mockMvc.perform(
                        get("/api/v1/book/%s".formatted(testUuid))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.message").value("Book with id %s not found".formatted(testUuid)))

                .andExpect(jsonPath("$.statusCode").exists())
                .andExpect(jsonPath("$.statusCode").isNumber())
                .andExpect(jsonPath("$.statusCode").value(404));
    }

    @Test
    void getAllBooks() throws Exception {

        mockMvc.perform(
                post("/api/v1/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
                        .content(objectMapper.writeValueAsString(
                                BookDTO.builder()
                                        .title("Int Test")
                                        .author("Int Test")
                                        .pubYear("0000")
                                        .build()
                        ))
        ).andExpect(status().isCreated());

        mockMvc.perform(
                post("/api/v1/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
                        .content(objectMapper.writeValueAsString(
                                BookDTO.builder()
                                        .title("Int Test 1")
                                        .author("Int Test 1")
                                        .pubYear("1111")
                                        .build()
                        ))
        ).andExpect(status().isCreated());

        mockMvc.perform(
                post("/api/v1/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
                        .content(objectMapper.writeValueAsString(
                                BookDTO.builder()
                                        .title("Int Test 3")
                                        .author("Int Test 3")
                                        .pubYear("1992")
                                        .build()
                        ))
        ).andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(
                get("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
        ).andExpect(status().isOk()).andReturn();

        List<BookDTO> resultList = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>(){}
        );

        assertFalse(resultList.isEmpty());
        assertEquals(resultList.size(), 3);

    }



    @Test
    void updateBook() throws Exception {
        MvcResult result = mockMvc.perform(
                post("/api/v1/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
                        .content(objectMapper.writeValueAsString(
                                BookDTO.builder()
                                        .title("Int Test")
                                        .author("Int Test")
                                        .pubYear("0000")
                                        .build()
                        ))
        ).andExpect(status().isCreated()).andReturn();

        String bookId = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDTO.class
        ).getId().toString();

        MvcResult updateResult = mockMvc.perform(
                put("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
                        .content(objectMapper.writeValueAsString(
                                BookDTO.builder()
                                        .id(UUID.fromString(bookId))
                                        .title("Updated Int Test")
                                        .author("Int Test")
                                        .pubYear("0000")
                                        .build()
                        ))
        ).andExpect(status().isOk()).andReturn();

        BookDTO updatedBook = objectMapper.readValue(
                updateResult.getResponse().getContentAsString(),
                BookDTO.class
        );

        assertEquals(updatedBook.getTitle(), "Updated Int Test");

    }

    @Test
    void deleteBook() throws Exception {
        MvcResult result = mockMvc.perform(
                post("/api/v1/book/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
                        .content(objectMapper.writeValueAsString(
                                BookDTO.builder()
                                        .title("Int Test")
                                        .author("Int Test")
                                        .pubYear("0000")
                                        .build()
                        ))
        ).andExpect(status().isCreated()).andReturn();

        String bookId = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDTO.class
        ).getId().toString();

        mockMvc.perform(
                delete("/api/v1/book/%s".formatted(bookId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(TEST_TOKEN))
                        .content(objectMapper.writeValueAsString(
                                BookDTO.builder()
                                        .id(UUID.fromString(bookId))
                                        .title("Updated Int Test")
                                        .author("Int Test")
                                        .pubYear("0000")
                                        .build()
                        ))
        ).andExpect(status().isOk());
    }
}
