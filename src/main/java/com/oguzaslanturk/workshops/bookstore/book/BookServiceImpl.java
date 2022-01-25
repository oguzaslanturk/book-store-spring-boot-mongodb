package com.oguzaslanturk.workshops.bookstore.book;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final MongoTemplate mongoTemplate;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, MongoTemplate mongoTemplate) {
        this.bookRepository = bookRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public BookDto getById(String id) {
        final Optional<Book> book = bookRepository.findById(id);
        return book.map(value -> modelMapper.map(value, BookDto.class)).orElse(null);
    }

    @Override
    public BookPageDto getAll(BookDto searchData, Pageable pageable) {
        Query query = new Query().with(pageable);

        if (StringUtils.isNotBlank(searchData.getName())) {
            query.addCriteria(Criteria.where("name").regex(Pattern.compile(searchData.getName(), Pattern.CASE_INSENSITIVE)));
        }
        if (StringUtils.isNotBlank(searchData.getIsbn())) {
            query.addCriteria(Criteria.where("isbn").is(searchData.getIsbn()));
        }

        final Page<Book> pageOfBooks = PageableExecutionUtils.getPage(
                mongoTemplate.find(query, Book.class),
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Book.class));


        return new BookPageDto(pageable.getPageNumber(),
                pageOfBooks.getTotalPages(),
                pageOfBooks.getTotalElements(),
                pageOfBooks.getContent()
                        .stream()
                        .map(book -> modelMapper.map(book, BookDto.class))
                        .collect(Collectors.toList()));
    }

    @Override
    public BookDto save(BookDto bookData) {
        Book book = modelMapper.map(bookData, Book.class);
        bookRepository.save(book);
        bookData.setId(book.getId());
        return bookData;
    }

    @Override
    public BookDto update(BookDto bookData) {
        Book book = modelMapper.map(bookData, Book.class);
        bookRepository.save(book);
        return modelMapper.map(book, BookDto.class);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }
}
