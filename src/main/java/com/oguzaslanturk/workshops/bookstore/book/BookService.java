package com.oguzaslanturk.workshops.bookstore.book;

import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto getById(String id);
    BookPageDto getAll(BookDto searchData, Pageable pageable);
    BookDto save(BookDto bookData);
    BookDto update(BookDto bookData);
    void deleteById(String id);
}
