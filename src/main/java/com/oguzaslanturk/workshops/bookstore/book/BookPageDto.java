package com.oguzaslanturk.workshops.bookstore.book;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class BookPageDto implements Serializable {
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private List<BookDto> books;
}
