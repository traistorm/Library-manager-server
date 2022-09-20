package com.example.librarymanage.Entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenid;
    private String tokenvalue;
    private String initializationtime;
    private String role;
    private Integer userid;
}
