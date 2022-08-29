package com.example.librarymanage.Entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "author")
public class Author {
    @Id
    private String authorid;
    private String authorname;
    private LocalDate dateofbirth;


}
