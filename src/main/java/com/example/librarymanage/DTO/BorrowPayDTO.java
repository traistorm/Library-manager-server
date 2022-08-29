package com.example.librarymanage.DTO;

import com.example.librarymanage.Entity.Book;
import com.example.librarymanage.Entity.BorrowPay;
import com.example.librarymanage.Entity.LibraryCard;
import com.example.librarymanage.Entity.Staff;
import lombok.Data;

import java.util.List;

@Data
public class BorrowPayDTO {
    private Integer maxPage;
    private List<BorrowPay> borrowPayList;
    private List<LibraryCard> libraryCardList;
    private List<Staff> staffList;
}
