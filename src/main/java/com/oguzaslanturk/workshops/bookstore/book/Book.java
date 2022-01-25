package com.oguzaslanturk.workshops.bookstore.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Document("books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String id;

    @NotBlank
    private String name;

    private String isbn;

    public Book(String name, String isbn) {
        this.name = name;
        this.isbn = isbn;
    }
}
