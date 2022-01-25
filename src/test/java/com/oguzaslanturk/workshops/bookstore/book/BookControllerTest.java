package com.oguzaslanturk.workshops.bookstore.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    private final static BookDto BOOK1 = new BookDto("1", "The Little Prince", "9786059681001");
    private final static BookDto BOOK2 = new BookDto("2", "Animal Farm", "9786257678322");
    private final static BookDto BOOK3 = new BookDto("3", "Alice's Adventures in Wonderland", "9786059681100");

    @Test
    public void givenExistingId_whenGetById_thenSuccess() throws Exception {
        Mockito.when(bookService.getById(BOOK1.getId())).thenReturn(BOOK1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("The Little Prince")));
    }

    @Test
    public void givenNonExistingId_whenGetById_thenNotFound() throws Exception {
        Mockito.when(bookService.getById("5")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNone_whenGetAll_thenFindThreeBooks() throws Exception {
        BookPageDto bookPageDto = new BookPageDto(0,
                1,
                3,
                new ArrayList<>(Arrays.asList(BOOK1, BOOK2, BOOK3)));

        Mockito.when(bookService.getAll(new BookDto(), PageRequest.of(0, 20))).thenReturn(bookPageDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalItems", is(3)))
                .andExpect(jsonPath("$.books", hasSize(3)))
                .andExpect(jsonPath("$.books[1].name", is("Animal Farm")));
    }

    @Test
    public void givenOneCharacterOfName_whenGetAll_thenFindOneBook() throws Exception {
        BookPageDto bookPageDto = new BookPageDto(0,
                1,
                1,
                new ArrayList<>(Arrays.asList(BOOK2)));

        BookDto bookDto = new BookDto();
        bookDto.setName("a");
        Mockito.when(bookService.getAll(bookDto, PageRequest.of(0, 20))).thenReturn(bookPageDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/?name=a")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalItems", is(1)))
                .andExpect(jsonPath("$.books", hasSize(1)))
                .andExpect(jsonPath("$.books[0].name", is(BOOK2.getName())));
    }

    @Test
    public void givenExactName_whenGetAll_thenFindOneBook() throws Exception {
        BookPageDto bookPageDto = new BookPageDto(0,
                1,
                1,
                new ArrayList<>(Arrays.asList(BOOK2)));

        BookDto bookDto = new BookDto();
        bookDto.setName(BOOK2.getName());
        Mockito.when(bookService.getAll(bookDto, PageRequest.of(0, 20))).thenReturn(bookPageDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/?name=" + BOOK2.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalItems", is(1)))
                .andExpect(jsonPath("$.books", hasSize(1)))
                .andExpect(jsonPath("$.books[0].name", is(BOOK2.getName())));
    }

    @Test
    public void givenNonExistingName_whenGetAll_thenFindNoBook() throws Exception {
        BookPageDto bookPageDto = new BookPageDto(0,
                0,
                0,
                new ArrayList<>());

        final String name = "a name that does not exist!";
        BookDto bookDto = new BookDto();
        bookDto.setName(name);
        Mockito.when(bookService.getAll(bookDto, PageRequest.of(0, 20))).thenReturn(bookPageDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/?name=" + name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(0)))
                .andExpect(jsonPath("$.totalItems", is(0)))
                .andExpect(jsonPath("$.books", is(empty())));
    }

    @Test
    public void givenExactIsbn_whenGetAll_thenFindOneBook() throws Exception {
        BookPageDto bookPageDto = new BookPageDto(0,
                1,
                1,
                new ArrayList<>(Arrays.asList(BOOK1)));

        BookDto bookDto = new BookDto();
        bookDto.setIsbn(BOOK1.getIsbn());
        Mockito.when(bookService.getAll(bookDto, PageRequest.of(0, 20))).thenReturn(bookPageDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/?isbn=" + BOOK1.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalItems", is(1)))
                .andExpect(jsonPath("$.books", hasSize(1)))
                .andExpect(jsonPath("$.books[0].name", is(BOOK1.getName())));
    }

    @Test
    public void givenNonExistingIsbn_whenGetAll_thenFindNoBook() throws Exception {
        BookPageDto bookPageDto = new BookPageDto(0,
                0,
                0,
                new ArrayList<>());

        final String nonExistingIsbn = "1111111111111";

        BookDto bookDto = new BookDto();
        bookDto.setIsbn(nonExistingIsbn);
        Mockito.when(bookService.getAll(bookDto, PageRequest.of(0, 20))).thenReturn(bookPageDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/?isbn=" + nonExistingIsbn)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(0)))
                .andExpect(jsonPath("$.totalItems", is(0)))
                .andExpect(jsonPath("$.books", is(empty())));
    }

    @Test
    public void givenBook_whenSave_thenSuccess() throws Exception {
        BookDto newBook = BookDto.builder()
                .id("4")
                .name("Peter Pan")
                .isbn("9786059489836")
                .build();

        Mockito.when(bookService.save(newBook)).thenReturn(newBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is("4")))
                .andExpect(jsonPath("$.name", is("Peter Pan")))
                .andExpect(jsonPath("$.isbn", is("9786059489836")));
    }

    @Test
    public void givenBook_whenUpdateNameAndIsbnForAnExistingId_thenSuccess() throws Exception {
        BookDto updatedBook = BookDto.builder()
                .id(BOOK3.getId())
                .name("Peter Pan")
                .isbn("9786059489836")
                .build();

        Mockito.when(bookService.getById(BOOK3.getId())).thenReturn(BOOK3);
        Mockito.when(bookService.update(updatedBook)).thenReturn(updatedBook);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is("3")))
                .andExpect(jsonPath("$.name", is("Peter Pan")))
                .andExpect(jsonPath("$.isbn", is("9786059489836")));
    }


    @Test
    public void givenBook_whenUpdateNameAndIsbnForNonExistingId_thenNotFound() throws Exception {
        BookDto updatedBook = BookDto.builder()
                .id("11")
                .name("Peter Pan")
                .isbn("9786059489836")
                .build();

        Mockito.when(bookService.update(updatedBook)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(updatedBook)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenExistingId_whenDelete_thenSuccess() throws Exception {
        Mockito.when(bookService.getById(BOOK1.getId())).thenReturn(BOOK1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + BOOK1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
    }


    @Test
    public void givenNonExistingId_whenDelete_thenNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/11")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }



}
