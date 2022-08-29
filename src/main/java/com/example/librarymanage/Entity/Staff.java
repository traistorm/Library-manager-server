package com.example.librarymanage.Entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "staff")
public class Staff {
    @Id
    private String staffid;
    private String staffname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateofbirth;
    private Integer sex;
}
