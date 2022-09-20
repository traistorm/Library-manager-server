package com.example.librarymanage.Entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;
    private String username;
    private String password;
    private String role;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate datecreated;
}
