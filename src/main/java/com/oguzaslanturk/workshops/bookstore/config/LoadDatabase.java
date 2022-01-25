package com.oguzaslanturk.workshops.bookstore.config;

import com.oguzaslanturk.workshops.bookstore.book.Book;
import com.oguzaslanturk.workshops.bookstore.book.BookRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    public CommandLineRunner initDatabase(BookRepository repository) {
        return args -> {
            log.info("Initializing database...");
            log.info("Inserting " + repository.save(new Book("1", "The Little Prince", "9786059681001")));
            log.info("Inserting " + repository.save(new Book("2", "Animal Farm", "9786257678322")));
        };
    }
}