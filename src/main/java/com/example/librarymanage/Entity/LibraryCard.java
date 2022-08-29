package com.example.librarymanage.Entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "librarycard")
@Data
public class LibraryCard {
    @Id
    private String librarycardid;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate starttime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate finishtime;
}
