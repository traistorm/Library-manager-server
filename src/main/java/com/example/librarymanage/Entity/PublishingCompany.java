package com.example.librarymanage.Entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "publishingcomany")
public class PublishingCompany {
    @Id
    private String publishingcompanyid;
    private String publishingcompanyname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishingcompanyfounddate;
}
