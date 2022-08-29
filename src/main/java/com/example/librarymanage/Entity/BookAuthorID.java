package com.example.librarymanage.Entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class BookAuthorID implements Serializable {
    private String bookid;
    private String authorid;
}
