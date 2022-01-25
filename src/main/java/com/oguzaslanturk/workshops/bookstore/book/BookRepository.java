package com.oguzaslanturk.workshops.bookstore.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    Page<Book> findByNameContainsIgnoreCase(Pageable pageable, String name);
    Page<Book> findByIsbn(Pageable pageable, String isbn);
    Page<Book> findByNameContainsIgnoreCaseOrIsbn(Pageable pageable, String name, String isbn);

}
