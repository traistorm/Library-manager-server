package com.example.librarymanage.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bookid", insertable = false, updatable = false) // thông qua khóa ngoại
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Book book;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "authorid", insertable = false, updatable = false) // thông qua khóa ngoại
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Author author;
}
