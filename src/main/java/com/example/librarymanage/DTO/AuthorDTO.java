package com.example.librarymanage.DTO;

import com.example.librarymanage.Entity.Author;
import lombok.Data;

import java.util.List;

@Data
public class AuthorDTO {
    private Integer maxPage;
    private List<Author> authorList;
}
