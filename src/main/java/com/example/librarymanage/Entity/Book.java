package com.example.librarymanage.Entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "book")
public class Book {
    @Id
    private String bookid;
    private String booktitle;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishingdate;
    private String publishingcompanyid;
    private Integer view;
    private Integer remainingamount;
    private String booklink;

}
