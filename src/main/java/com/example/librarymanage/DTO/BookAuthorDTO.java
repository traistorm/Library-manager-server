package com.example.librarymanage.DTO;

import com.example.librarymanage.Entity.BookAuthor;
import lombok.Data;

import java.util.List;

@Data
public class BookAuthorDTO {
    private Integer maxPage;
    private List<BookAuthor> bookAuthorList;
}
