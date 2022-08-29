package com.example.librarymanage.DTO;

import com.example.librarymanage.Entity.Book;
import lombok.Data;

import java.util.List;

@Data
public class BookDTO {
    private Integer maxPage;
    private List<Book> bookList;
}
