package com.example.librarymanage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "bookauthor")
@IdClass(BookAuthorID.class)
@AllArgsConstructor
public class BookAuthor {
    @Id
    private String bookid;
    @Id
    private String authorid;


    public BookAuthor() {

    }
}
