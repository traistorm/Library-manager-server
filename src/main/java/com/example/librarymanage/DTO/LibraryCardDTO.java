package com.example.librarymanage.DTO;

import com.example.librarymanage.Entity.LibraryCard;
import lombok.Data;

import java.util.List;

@Data
public class LibraryCardDTO {
    private Integer maxPage;
    private List<LibraryCard> libraryCardList;
}
