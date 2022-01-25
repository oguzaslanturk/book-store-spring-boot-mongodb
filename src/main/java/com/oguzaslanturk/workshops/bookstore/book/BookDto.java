package com.oguzaslanturk.workshops.bookstore.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private String id;
    private String name;
    @Size(min = 11, max = 13)
    private String isbn;
}
